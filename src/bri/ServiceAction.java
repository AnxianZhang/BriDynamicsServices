package bri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface ServiceAction {
    void performServiceAction(Programmer p, PrintWriter pSocketOut, BufferedReader pSocketIn) throws IOException;
}
