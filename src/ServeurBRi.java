import java.io.IOException;
import java.net.ServerSocket;

public class ServeurBRi implements Runnable {
    private ServerSocket listen_socket;
    public ServeurBRi(int port) {
        try {
            listen_socket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while(true)
                new ServiceBRi(listen_socket.accept()).start();
        }
        catch (IOException e) {
            try {this.listen_socket.close();} catch (IOException e1) {}
            System.err.println("Pb sur le port d'écoute :"+e);
        }
    }

    protected void finalize() throws Throwable {
        try {this.listen_socket.close();} catch (IOException e1) {}
    }

    // lancement du serveur
    public void lancer() {
        (new Thread(this)).start();
    }
}
