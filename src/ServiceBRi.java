import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceBRi implements Runnable{
    private Socket client;
    public ServiceBRi(Socket socket) {
        client = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
            PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
            int choix = Integer.parseInt(in.readLine());
            if(client.getPort()==5201) {
                out.println(ServiceAmateurs.toStringue() + "##Tapez le numéro de service désiré :");
                ServiceAmateurs.getServiceClass(choix).getConstructor(Socket.class).newInstance(this.client).run();
            }
            if(client.getPort()==1314) {
                out.println(ServiceProgrammeurs.toStringue() + "##Tapez le numéro de service désiré :");
                ServiceProgrammeurs.getActivite(choix);
            }
        }
        catch (Exception e) {
            //Fin du service
        }

        try {client.close();} catch (IOException e2) {}
    }

    protected void finalize() throws Throwable {
        client.close();
    }

    public void start()  {
        (new Thread(this)).start();
    }
}
