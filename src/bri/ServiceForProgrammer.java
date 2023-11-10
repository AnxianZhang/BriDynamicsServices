package bri;

import bri.service_action.AddServiceAction;
import bri.service_action.UpdateServiceAction;

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
            super.getSockOut().println("Enter a correct new FTP ulr (e.g ftp://localhost:2121/myDir/): ");
            newUrl = super.getSockIn().readLine();
        }

        this.currentProgrammer.setFtpUrl(newUrl);
        super.getSockOut().println("Url Changed, press a key to continue##");
        super.getSockIn().readLine();
    }

    private void startActivity(int choice) throws IOException {
        switch (choice) {
            case 1:
                ServiceAction sAdd = new AddServiceAction();
                sAdd.performServiceAction(this.currentProgrammer, super.getSockOut(), super.getSockIn());
                break;
            case 2:
                ServiceAction sUpdate = new UpdateServiceAction();
                sUpdate.performServiceAction(this.currentProgrammer, super.getSockOut(), super.getSockIn());
                break;
            case 3:
                changeProgrammerFtpUrl();
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                System.out.println("Choix invalide");
        }
    }

    private boolean isSearchAccountActivities(String s) {
        if (super.isDigit(s)) {
            int num = Integer.parseInt(s);
            return num == 1 || num == 2;
        }
        return false;
    }

    private boolean isFtpUrlCorrect(String ftpUrl) {
        try {
            URLClassLoader.newInstance(new URL[]{new URL(ftpUrl)});
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private String getProgrammerFtpUrl() throws IOException {
        String ftpUrl = "";
        while (!isFtpUrlCorrect(ftpUrl)) {
            super.getSockOut().println("Enter a correct FTP url (e.g ftp://localhost:2121/myDir/): ");
            ftpUrl = super.getSockIn().readLine();
        }
        return ftpUrl;
    }

    private boolean createAccount(String login, String pwd) throws IOException {
        String ftpUrl = getProgrammerFtpUrl();

        try {
            this.currentProgrammer = ServiceRegistry.addProgrammer(login, pwd, ftpUrl);
            super.getSockOut().println("Account created, press to continue##");
            super.getSockIn().readLine();
            return true;

        } catch (Exception e) {
            super.getSockOut().println(e.getMessage() + " press a key to retry##");
            super.getSockIn().readLine();
            return false;
        }
    }

    private boolean connectionToAccount(String login, String pwd) throws IOException {
        Programmer p = ServiceRegistry.getProgrammer(login, pwd);
        if (p != null) {
            super.getSockOut().println("You are now connected, press a key to continue##");
            super.getSockIn().readLine();
            this.currentProgrammer = p;
            return true;
        } else {
            super.getSockOut().println("Connection problem, press a key to retry##");
            super.getSockIn().readLine();
            return false;
        }
    }

    private boolean isAccountHandlerSuccess(int num) throws IOException {
        super.getSockOut().println("Enter your login: ");
        String login = super.getSockIn().readLine();
        super.getSockOut().println("Enter your password: ");
        String pwd = super.getSockIn().readLine();

        return num == 1 ? connectionToAccount(login, pwd) : createAccount(login, pwd);
    }

    private void searchAccount() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("1. Sign in##");
        sb.append("2. Sign up##");
        super.getSockOut().println(sb.toString());

        while (true) {
            String msgCli = super.getSockIn().readLine();

            if (msgCli.equals("quit")) {
                break;
            }

            if (isSearchAccountActivities(msgCli)) {
                int num = Integer.parseInt(msgCli);
                if (isAccountHandlerSuccess(num))
                    break;
                else
                    super.getSockOut().println(sb.toString());
            } else {
                super.getSockOut().println("You must enter a digit corresponding to one of the following service numbers: ");
            }
        }
    }

    @Override
    protected boolean numActivityToLaunchPrecondition() {
        return this.currentProgrammer == null;
    }

    @Override
    protected void numActivityToLaunchPreconditionMessage() {
        /* Empty because we don't need to send a message when getting num activity */
    }

    @Override
    protected void showAllPossibleActivities() {
        super.getSockOut().println(showActivities());
    }

    @Override
    protected void startTheSpecificActivity(int num) throws IOException {
        startActivity(num);
        super.getSockOut().println(showActivities());
    }

    @Override
    protected boolean isAnActivityNumberInterval(int num) {
        return num <= 3 && num >= 1;
    }

    @Override
    public void run() {
        System.out.println("========== Client connection " + super.getSocketClient().getInetAddress() + " ==========");
        super.getSockOut().println("++++++++++ Welcome to the programmer service ++++++++++");
        try {
            searchAccount();
            super.numActivityToLaunch();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            super.closeSocketClient();
        }
    }
}