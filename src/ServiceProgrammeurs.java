import java.io.IOException;
import java.net.Socket;

public abstract class ServiceProgrammeurs extends Service{
    private ServiceClient serviceClient;
    public ServiceProgrammeurs(Socket socketClient) throws IOException {
        super(socketClient);
    }

    public static String toStringue() {
        StringBuilder activites = new StringBuilder();
        activites.append("Activités programmeurs :##");
        activites.append("1. Fournir un nouveau service ##");
        activites.append("2. Mettre-à-jour un service ##");
        activites.append("3. Déclarer un changement d’adresse de votre serveur FTP ##");
        activites.append("4. (Démarrer/arrêter un service )##");
        activites.append("5. (Désinstaller un service )##");
        return activites.toString();
    }

    public static void getActivite(int choix) {
        switch (choix) {
            case 1:
                // Fournir un nouveau service
                break;
            case 2:
                // Mettre-à-jour un service
                break;
            case 3:
                // changement d’adresse FTP
                break;
            case 4:
                // Démarrer/arrêter un service
                break;
            case 5:
                // Désinstaller un service
                break;
            default:
                System.out.println("Choix invalide");
        }
    }
}
