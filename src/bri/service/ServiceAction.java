package bri.service;

import bri.Programmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public interface ServiceAction {
    void performServiceAction(Programmer p, PrintWriter pSocketOut, BufferedReader pSocketIn, Socket s) throws IOException;
}
