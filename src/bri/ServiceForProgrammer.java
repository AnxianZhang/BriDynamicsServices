package bri;

import person.Programmer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

public class ServiceForProgrammer extends ServiceClient {
    private Programmer currentProgrammer;

    public ServiceForProgrammer(Socket socketClient) throws IOException {
        super(socketClient);

        this.currentProgrammer = null;
    }

    public String showActivities() {
        StringBuilder activites = new StringBuilder();
        activites.append("Our activities for programmers:##");
        activites.append("1. Provide a new service##");
        activites.append("2. Update a service##");
        activites.append("3. Declare a change of FTP server address##");

        // ========== bonus ==========
//        activites.append("4. (Démarrer/arrêter un service)##");
//        activites.append("5. (Désinstaller un service )##");
        return activites.toString();
    }

    private void changeProgrammerFtpUrl() throws IOException {
        String newUrl = "";
        while (!isFtpUrlCorrect(newUrl)) {
            super.println("Enter a correct new FTP ulr (e.g ftp://localhost:2121/myDir/): ");
            newUrl = super.getSockIn().readLine();
        }

        this.currentProgrammer.setFtpUrl(newUrl);
        super.println("Url Changed, press a key to continue##");
        super.getSockIn().readLine();
    }

    private void addNewService () throws IOException {
        super.println("Enter the service you want to add: ");
        String className = super.getSockIn().readLine();

        // ftp://localhost:2121/classes/
        // ftp://localhost:2121/filesErr/

        /**
         * ========== /!\ ==========
         * ftp://localhost:2121/filesErr/ != ftp://localhost:2121/filesErr
         * avec / -> l'url classloader pointera vers un DIR et le chargement se fait depuis le FICHIER LUI MEME
         * sans / -> il pointe aussi vers le DIR mais il va charger tout le contenu du fichier .jar
         */

        try {
            /* on met pas Class <? extends Service> car si la classe implement pas il y aura une exception
             * ClassNotFoundException qui va être lever, nous on veux que ça soit une new Exception
             * de ServiceRegistry.addService qui soit lever
             * */
            Class<?> classToCharge = this.currentProgrammer.laodClass(className);
            ServiceRegistry.addService(classToCharge, this.currentProgrammer);
            return;
        } catch (ClassNotFoundException e) {
            super.println("The class: " + className + " isn't inside FTP server, press a key to retry##");
        } catch (Exception e){
            super.println(e.getMessage() + " press a key to retry##");
        }
        super.getSockIn().readLine();
    }

    private void updateService() throws IOException {
        String classToRecharge = "";
        try {
            super.println(ServiceRegistry.getListServicesOfProg(currentProgrammer).toString() + "##Enter the class name of service you want to update: ");
            classToRecharge = super.getSockIn().readLine();
            Class<?> classToCharge = this.currentProgrammer.laodClass(classToRecharge);
            ServiceRegistry.updateService(classToCharge, this.currentProgrammer);
            return;
        } catch (ClassNotFoundException e) {
            super.println("The class: " + classToRecharge + " isn't inside FTP server, press a key to retry##");
        } catch (Exception e) {
            super.println(e.getMessage() + "##Press a key to retry##");
        }
        super.getSockIn().readLine();
    }

    private void startActivity(int choice) throws IOException {
        switch (choice) {
            case 1:
                /**
                 * URLClassLoader garde en memoire les classes charger, donc meme si on supp la class du dossier
                 * la classe continue d'exister dans la memoire du JVM tant qu'elle est reférencer dans le code
                 */
//                super.println("Enter the service you want to add: ");
//                String className = super.getSockIn().readLine();
                // add new service
                addNewService();
                break;
            case 2:
                // Mettre-à-jour un service
                updateService();
                break;
            case 3:
                // Change of address
                changeProgrammerFtpUrl();
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

    private boolean isDigit(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isAnActivityNumber(String s) {
        if (isDigit(s)){
            int num = Integer.parseInt(s);
            return num <= 3 && num >= 1;
        }
        return false;
    }

    private boolean isSearchAccountActivities(String s) {
        if (isDigit(s)){
            int num = Integer.parseInt(s);
            return num == 1 || num == 2;
        }
        return false;
    }

    private boolean isFtpUrlCorrect (String ftpUrl){
        try {
            URLClassLoader.newInstance(new URL[] { new URL(ftpUrl)});
            return true;
        } catch (MalformedURLException e){
            return false;
        }
    }

    private boolean createAccount(String login, String pwd) throws IOException {
        String ftpUrl = "";
        while (!isFtpUrlCorrect(ftpUrl)){
            super.println("Enter a correct FTP url (e.g ftp://localhost:2121/myDir/): ");
            ftpUrl = super.getSockIn().readLine();
        }

        try {
            this.currentProgrammer = ServiceRegistry.addProgrammer(login, pwd, ftpUrl);

            super.println("Account created, press to continue##");
            super.getSockIn().readLine();
            return true;
        }catch (Exception e) {
            super.println(e.getMessage() + " press a key to retry##");
            super.getSockIn().readLine();
            return false;
        }
    }

    private boolean connectionToAccount(String login, String pwd) throws IOException {
//        while (true) {
        Programmer p = ServiceRegistry.getProgrammer(login, pwd);
        if (p != null) {
            super.println("You are now connected, press a key to continue##");
            super.getSockIn().readLine();
            this.currentProgrammer = p;

            return true;
        }
        else {
            super.println("Connection problem, press a key to retry##");
            super.getSockIn().readLine();
            return false;
        }
//        }

    }

    private boolean isAccountHandlerSuccess(int num) throws IOException {
        super.println("Enter your login: ");
        String login = super.getSockIn().readLine();
        super.println("Enter your password: ");
        String pwd = super.getSockIn().readLine();

        if (num == 1){
            return connectionToAccount(login, pwd);
        }
        else {
            return createAccount(login, pwd);
        }
    }

    private void searchAccount() throws IOException {
        StringBuilder sb = new StringBuilder(""); // sb = 傻逼 嘿嘿黑
        sb.append("1. Sign in##");
        sb.append("2. Sign up##");
        super.println(sb.toString());

        while (true){
            String msgCli = super.getSockIn().readLine();

            if (msgCli.equals("quit")) {
                break;
            }

            if (isSearchAccountActivities(msgCli)){
                int num = Integer.parseInt(msgCli);
                if (isAccountHandlerSuccess(num))
                    break;
                else
                    super.println(sb.toString());
            }
            else {
                super.println("You must enter a digit corresponding to one of the following service numbers: ");
            }
        }
    }

    private void getNumActivityToLaunch() throws IOException {
        if (this.currentProgrammer == null){
            return;
        }

        super.println(showActivities());

        while (true) {
            String msgCli = super.getSockIn().readLine();

            if (msgCli.equals("quit")) {
                break;
            }

            if (isAnActivityNumber(msgCli)) {
                int num = Integer.parseInt(msgCli);
                startActivity(num);
                super.println(showActivities());
            } else {
                super.println("You must enter a digit corresponding to one of the following service numbers: ");
            }
        }
    }

    @Override
    public void run() {
        System.out.println("========== Client connection " + super.getSocketClient().getInetAddress() + " ==========");
        super.println("++++++++++ Welcome to the programmer service ++++++++++");
        try {
            searchAccount();
            getNumActivityToLaunch();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            super.closeSocketClient();
        }
    }
}