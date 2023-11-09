package app;

import server.ServeurBRi;
import bri.ServiceAmateur;
import bri.ServiceForProgrammer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

public class BriLaunch {
    private static final int PROGRAMMER_PORT;
    private static final int AMATEUR_PORT;

    static {
        AMATEUR_PORT = 5201;
        PROGRAMMER_PORT = 1314;
    }

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        System.out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
//        System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
//        System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");

//        new Thread(new server.ServeurBRi(PROGRAMMER_PORT)).start();
//        new Thread(new server.ServeurBRi(AMATEUR_PORT)).start();

        ServeurBRi sProgrammer = new ServeurBRi(ServiceForProgrammer.class, PROGRAMMER_PORT);
        ServeurBRi sAmateur = new ServeurBRi(ServiceAmateur.class, AMATEUR_PORT);

        Thread t1 = new Thread(sProgrammer);
        Thread t2 = new Thread(sAmateur);

        t1.start();
        t2.start();
    }
}