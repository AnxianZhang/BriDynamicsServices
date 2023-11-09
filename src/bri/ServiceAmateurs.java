package bri;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

public class ServiceAmateurs extends GeneralService {
//    private static List<Class<? extends service.Service>> servicesClasses;
//    static {
//        servicesClasses = new Vector<>();
//    }
    private static ServiceClient serviceClient;
    private List<Class <? extends Service>> allServiceClasses;

    public ServiceAmateurs(Socket socketClient) throws IOException {
        super(socketClient);
        //allServiceClasses=ServiceRegistry.getAllServices();
    }

    public String toStringue() {
        StringBuilder r = new StringBuilder();
        r.append("Activités présentes :##");
        int i= 1;
        synchronized (allServiceClasses) {
            for (Class<? extends Service> s: allServiceClasses) {
                try {
                    Method toStringue =s.getMethod("toStringue");
                    String ts = (String) toStringue.invoke(s);
                    r.append(i).append(" . ").append(ts).append("##");
                    ++i;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
//        System.out.println(r.toString());
        return r.toString();
    }

    public static Class<? extends Runnable> getServiceClass(int numService) {
        return serviceClient.getServiceClass(numService - 1);
    }

    private void getNumActivityToLaunch() throws IOException{
        super.println(toStringue());

        while (true) {
            String msgCli = super.getSockIn().readLine();

            if (msgCli.equals("quit")) {
                break;
            }

            if (isAnActivityNumber(msgCli)) {
                System.out.println("num Activity: " + msgCli);
                super.println("Vous avez choisi le service num: " + msgCli + "##");
                int num = Integer.parseInt(msgCli);
                // todo (start the activity)
                break;
            } else {
                super.println("You must enter a digit corresponding to one of the following service numbers: ");
            }
        }
    }

    private boolean isAnActivityNumber(String msgCli) {
        try {
            int num=Integer.parseInt(msgCli);
            return num<=allServiceClasses.size() && num>=1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void run() {
        System.out.println("========== Client connection " + super.getSocketClient().getInetAddress() + " ==========");
        super.println("++++++++++ Welcome to the amateur service ++++++++++");
        try {
            getNumActivityToLaunch();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
