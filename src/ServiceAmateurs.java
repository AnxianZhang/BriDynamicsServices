import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public abstract class ServiceAmateurs extends Service{
//    private static List<Class<? extends Service>> servicesClasses;
//    static {
//        servicesClasses = new Vector<>();
//    }
    private static ServiceClient serviceClient;

    public ServiceAmateurs(Socket socketClient) throws IOException {
        super(socketClient);
    }

    public static String toStringue() {
        return null;
        //class data
    }

    public static Class<? extends Runnable> getServiceClass(int numService) {
        return serviceClient.getServiceClass(numService - 1);
    }
}
