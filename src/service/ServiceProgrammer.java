package service;

import person.Person;
import person.Programmer;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class ServiceProgrammer extends ServiceClient {
    private Person currentProgrammer;

    public ServiceProgrammer(Socket socketClient) throws IOException {
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

    public void getActivite(int choix) {
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

    private void createAccount() throws IOException {
        super.println("Enter your login: ");
        String login = super.getSockIn().readLine();
        super.println("Enter your password: ");
        String pwd = super.getSockIn().readLine();

        ServiceRegistry.addProgrammer(login, pwd);

        super.println("Account created, press to continue##");
        super.getSockIn().readLine();
    }

    private void connectionToAccount() throws IOException {
        while (true) {
            super.println("Enter your login: ");
            String login = super.getSockIn().readLine();
            super.println("Enter your password: ");
            String pwd = super.getSockIn().readLine();

            Person p = ServiceRegistry.getProgrammer(login, pwd);
            if (p != null) {
                super.println("You are now connected, press a key to continue##");
                this.currentProgrammer = p;
                break;
            }
            else {
                super.println("Connection problem, press a key to retry##");
            }
            super.getSockIn().readLine();
        }

    }

    private void accountHandler(int num) throws IOException {
        if (num == 1){
            connectionToAccount();
        }
        else {
            createAccount();
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
                System.out.println("num Activity account: " + msgCli);
                int num = Integer.parseInt(msgCli);
                accountHandler(num);
                break;
            }
            else {
                super.println("You must enter a digit corresponding to one of the following service numbers: ");
            }
        }
    }

    private void getNumActivityToLaunch() throws IOException {
        super.println(showActivities());

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
