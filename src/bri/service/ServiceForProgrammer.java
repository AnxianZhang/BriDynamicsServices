package bri.service;

import bri.Programmer;
import bri.ServiceRegistry;

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
        return "Our activities for programmers:##" +
                "1. Provide a new service##" +
                "2. Update a service##" +
                "3. Declare a change of FTP server address##";
    }

    private void changeProgrammerFtpUrl() throws IOException {
        String newUrl = "";
        while (!isFtpUrlCorrect(newUrl)) {
            super.getSockOut().println("Enter a correct new FTP ulr (e.g ftp://localhost:2121/myDir/): ");
            newUrl = ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
        }

        this.currentProgrammer.setFtpUrl(newUrl);
        super.getSockOut().println("Url Changed, press a key to continue##");
        ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
    }

    private void performServiceAction(String action) throws IOException {
        String className = "";
        try {
            super.getSockOut().println(action.equals("add") ?
                    "Enter the service you want to add: " :
                    ServiceRegistry.getListServicesOfProg(currentProgrammer).toString() +
                            "##Enter the class name of service you want to update: "
            );
            className = ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
            Class<?> classToCharge = this.currentProgrammer.loadClass(className);

            // it's in addService/updateService that we cast Class<?> to Class<? extends Service> with .asSubclass
            if (action.equals("add"))
                ServiceRegistry.addService(classToCharge, this.currentProgrammer);
            else
                ServiceRegistry.updateService(classToCharge, this.currentProgrammer);

            super.getSockOut().println(className + (action.equals("add") ?
                    " is now added##Press a key to continue##" :
                    " updated##Press a key to continue##")
            );
        } catch (ClassNotFoundException e) {
            super.getSockOut().println(className + " isn't inside FTP server, press a key to retry##");
        } catch (Exception e) {
            super.getSockOut().println(e.getMessage() + " press a key to retry##");
        }
        ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
    }

    private void startActivity(int choice) throws IOException {
        switch (choice) {
            case 1:
                performServiceAction("add");
                break;
            case 2:
                performServiceAction("update");
                break;
            case 3:
                changeProgrammerFtpUrl();
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
            ftpUrl = ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
        }
        return ftpUrl;
    }

    private boolean createAccount(String login, String pwd) throws IOException {
        String ftpUrl = getProgrammerFtpUrl();

        try {
            this.currentProgrammer = ServiceRegistry.addProgrammer(login, pwd, ftpUrl);
            super.getSockOut().println("Account created, press to continue##");
            ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
            return true;

        } catch (Exception e) {
            super.getSockOut().println(e.getMessage() + " press a key to retry##");
            ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
            return false;
        }
    }

    private boolean connectionToAccount(String login, String pwd) throws IOException {
        Programmer p = ServiceRegistry.getProgrammer(login, pwd);
        if (p != null) {
            super.getSockOut().println("You are now connected, press a key to continue##");
            ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
            this.currentProgrammer = p;
            return true;
        } else {
            super.getSockOut().println("Connection problem, press a key to retry##");
            ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
            return false;
        }
    }

    private boolean isAccountHandlerSuccess(int num) throws IOException {
        super.getSockOut().println("Enter your login: ");
        String login = ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
        super.getSockOut().println("Enter your password: ");
        String pwd = ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());

        return num == 1 ? connectionToAccount(login, pwd) : createAccount(login, pwd);
    }

    private void searchAccount() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("1. Sign in##");
        sb.append("2. Sign up##");
        super.getSockOut().println(sb);

        while (true) {
            String msgCli = ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());

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
            super.timeOutMsg();
        } finally {
            super.closeSocketClient();
        }
    }
}