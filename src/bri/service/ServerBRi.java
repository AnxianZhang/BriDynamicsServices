package bri.service;

import bri.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBRi implements Runnable {
    private final int numPort;
    private final Class<? extends Service> service;
    private final ServerSocket myServer;
    public ServerBRi(Class<? extends Service> service, int port) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        service.getConstructor(Socket.class).newInstance(new Socket());

        this.service = service;
        this.numPort = port;
        this.myServer = new ServerSocket(this.numPort);
    }

    @Override
    public void run() {
        System.out.println("========== Server to port " + this.numPort + " created ==========");

        try {
            while(true){
                Socket socket = myServer.accept();
                (new Thread (service.getConstructor(Socket.class).newInstance(socket))).start();
            }
        }
        catch (IOException e) {
            try {this.myServer.close();} catch (IOException e1) {}
            System.err.println("Problem in listening port :"+e);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            // already tested in constructor
        }
    }
}
