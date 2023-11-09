package bri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * la classe pourra être fusioner avec ServiceClient
 * ==> à voir s'il y a de la place
 */
public abstract class GeneralService implements Service {
    private final Socket socketClient;
    private final BufferedReader sockIn;
    private final PrintWriter sockOut;

    public GeneralService(Socket socketClient) throws IOException {
        this.socketClient = socketClient;

        BufferedReader sockIn1;
        PrintWriter sockOut1;
        try {
            sockIn1 = new BufferedReader(new InputStreamReader(this.socketClient.getInputStream()));
            sockOut1 = new PrintWriter(this.socketClient.getOutputStream(), true);
        } catch (SocketException var5) {
            sockOut1 = null;
            sockIn1 = null;
        }

        this.sockIn = sockIn1;
        this.sockOut = sockOut1;
    }

    protected Socket getSocketClient() {
        return this.socketClient;
    }

    protected BufferedReader getSockIn() {
        return this.sockIn;
    }

    protected void println(String msg) {
        this.sockOut.println(msg);
    }
}