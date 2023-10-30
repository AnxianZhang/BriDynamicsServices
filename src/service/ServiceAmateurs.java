package service;

import java.io.IOException;
import java.net.Socket;

public class ServiceAmateurs extends Service {
//    private static List<Class<? extends service.Service>> servicesClasses;
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

    @Override
    public void run() {

    }
}
