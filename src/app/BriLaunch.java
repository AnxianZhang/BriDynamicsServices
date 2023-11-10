package app;

import bri.ServerBRi;
import bri.service.ServiceAmateur;
import bri.service.ServiceForProgrammer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class BriLaunch {
    private static final int PROGRAMMER_PORT;
    private static final int AMATEUR_PORT;

    static {
        AMATEUR_PORT = 5201;
        PROGRAMMER_PORT = 1314;
    }

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ServerBRi sProgrammer = new ServerBRi(ServiceForProgrammer.class, PROGRAMMER_PORT);
        ServerBRi sAmateur = new ServerBRi(ServiceAmateur.class, AMATEUR_PORT);

        Thread t1 = new Thread(sProgrammer);
        Thread t2 = new Thread(sAmateur);

        t1.start();
        t2.start();
    }
}