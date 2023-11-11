package bri.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ReceptionTimeOut {
    private static final Timer t;
    private static final long TIME_BEFORE_TIMEOUT;

    static {
        t = new Timer();
        TIME_BEFORE_TIMEOUT = 1000L * 60 * 10;
    }

    private ReceptionTimeOut() { // avoid any new instance of that class
        throw new IllegalStateException("ReceptionTimeOut is an utility class");
    }

    private static class TimeOut extends TimerTask {
        private final Socket sockCli;

        public TimeOut(Socket s) {
            this.sockCli = s;
        }

        @Override
        public void run() {
            try {
                this.sockCli.close();
            } catch (IOException e) {
                System.out.println("Problem when closing timer of ReceptionTimeOut");
            }
        }
    }

    public static void closeTimeOut() { // will be use on day when we want to restart/stop the server
        t.cancel();
    }

    public static String receive(BufferedReader sockIn, Socket sockCli) throws IOException {
        TimeOut timeOut = new TimeOut(sockCli);
        t.schedule(timeOut, TIME_BEFORE_TIMEOUT);

        String cliRep = sockIn.readLine();
        timeOut.cancel();

        return cliRep;
    }
}