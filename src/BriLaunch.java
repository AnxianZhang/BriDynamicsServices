import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
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
        Scanner clavier = new Scanner(System.in);
        String fileNameURL = "ftp://localhost:2121/";

        URLClassLoader urlcl = URLClassLoader.newInstance(new URL[] { new URL(fileNameURL)});

        System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
//        System.out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
//        System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
//        System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");

//        new Thread(new ServeurBRi(PROGRAMMER_PORT)).start();
//        new Thread(new ServeurBRi(AMATEUR_PORT)).start();

        ServeurBRi sProgrammer = new ServeurBRi(ServiceProgrammeurs.class, PROGRAMMER_PORT);
        ServeurBRi sAmateur = new ServeurBRi(ServiceAmateurs.class, AMATEUR_PORT);
    }
}