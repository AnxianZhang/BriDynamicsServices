package bri.service;

import bri.ReceptionTimeOut;
import bri.service.GeneralService;

import java.io.IOException;
import java.net.Socket;

public abstract class ServiceClient extends GeneralService {
    protected ServiceClient(Socket socketClient) throws IOException {
        super(socketClient);
    }

    protected abstract boolean numActivityToLaunchPrecondition();

    protected abstract void numActivityToLaunchPreconditionMessage() throws IOException;

    protected abstract void showAllPossibleActivities();

    protected abstract void startTheSpecificActivity(int num) throws IOException;

    protected abstract boolean isAnActivityNumberInterval(int num);

    protected void timeOutMsg() {
        System.out.println("Time out of: " + super.getSocketClient().getInetAddress());
    }

    protected boolean isDigit(String s) {
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
            return isAnActivityNumberInterval(num);
        }
        return false;
    }

    protected void numActivityToLaunch() throws IOException {
        if (numActivityToLaunchPrecondition()) {
            numActivityToLaunchPreconditionMessage();
            return;
        }

        showAllPossibleActivities();

        while (true) {
            String msgCli = ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());

            if (msgCli.equals("quit")) {
                break;
            }

            if (isAnActivityNumber(msgCli)) {
                int num = Integer.parseInt(msgCli);
                startTheSpecificActivity(num);
            } else {
                super.getSockOut().println("You must enter a digit corresponding to one of the following service numbers: ");
            }
        }
    }

    protected void closeSocketClient() {
        try {
            super.getSocketClient().close();
            System.out.println("========== Client disconnection " + super.getSocketClient().getInetAddress() + " ==========");
            System.out.println();
        } catch (IOException e) {
            System.out.println("Problem when closing socket in ServiceClient");
        }
    }
}