package server;

import service.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurBRi implements Runnable {
    private final int NUM_PORT;
    private final Class<? extends Service> service;
    private final ServerSocket myServer;
    public ServeurBRi(Class<? extends Service> service, int port) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        try {
//            myServer = new ServerSocket(port);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        service.getConstructor(Socket.class).newInstance(new Socket());

        this.service = service;
        this.NUM_PORT = port;
        this.myServer = new ServerSocket(this.NUM_PORT);
    }

    @Override
    public void run() {
        System.out.println("========== Server au port " + this.NUM_PORT + " cree ==========");

        try {
            while(true){
                Socket socket = myServer.accept();
                (new Thread (service.getConstructor(Socket.class).newInstance(socket))).start();
            }
//                new ServiceBRi(myServer.accept()).start();
        }
        catch (IOException e) {
            try {this.myServer.close();} catch (IOException e1) {}
            System.err.println("Pb sur le port d'écoute :"+e);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            // already tested in constructor
        }
    }
//
//    protected void finalize() throws Throwable {
//        try {this.myServer.close();} catch (IOException e1) {}
//    }

    // lancement du serveur
    public void lancer() {
        (new Thread(this)).start();
    }
}
