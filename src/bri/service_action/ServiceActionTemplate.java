package bri.service_action;

import bri.ServiceAction;
import bri.Programmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class ServiceActionTemplate implements ServiceAction {
    public void performServiceAction(Programmer p, PrintWriter pSocketOut, BufferedReader pSocketIn) throws IOException {
        String className = "";
        try {
            pSocketOut.println(promptMessage(p));
            className = pSocketIn.readLine();
            Class<?> classToCharge = p.loadClass(className);
            // it's in addService that we cast Class<?> to Class<? extends Service> with .asSubclass
            handleAction(classToCharge, p);
            pSocketOut.println(serviceActionMessage(classToCharge));
        } catch (ClassNotFoundException e) {
            pSocketOut.println(className + " isn't inside FTP server, press a key to retry##");
        } catch (Exception e) {
            pSocketOut.println(e.getMessage() + " press a key to retry##");
        }
        pSocketIn.readLine();
    }

    protected abstract String promptMessage(Programmer p) throws Exception;
    protected abstract void handleAction(Class<?> c, Programmer p) throws Exception;
    protected abstract String serviceActionMessage(Class<?> c);
}
