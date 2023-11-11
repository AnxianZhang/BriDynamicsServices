package bri.service;

import bri.Service;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class ServiceAmateur extends ServiceClient {
    public ServiceAmateur(Socket socketClient) throws IOException {
        super(socketClient);
    }

    private void launchActivity(int num) {
        try {
            Constructor<? extends Service> constructor = ServiceRegistry.getServiceClass(num).getConstructor(Socket.class);
            Service s = constructor.newInstance(this.getSocketClient());
            s.run();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.out.println("Problem occurred in launchActivity of AmateursService");
        }
    }

    @Override
    protected boolean numActivityToLaunchPrecondition() {
        return ServiceRegistry.getServicesSize() == 0;
    }

    @Override
    protected void numActivityToLaunchPreconditionMessage() throws IOException {
        super.getSockOut().println("No services are now available, enter 'quit' to finish process##");
        ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
    }

    @Override
    protected void showAllPossibleActivities() {
        super.getSockOut().println(ServiceRegistry.toStringue());
    }

    @Override
    protected void startTheSpecificActivity(int num) throws IOException {
        launchActivity(num);
        ReceptionTimeOut.receive(super.getSockIn(), super.getSocketClient());
        super.getSockOut().println(ServiceRegistry.toStringue());
    }

    @Override
    protected boolean isAnActivityNumberInterval(int num) {
        return num <= ServiceRegistry.getServicesSize() && num >= 1;
    }

    @Override
    public void run() {
        System.out.println("========== Client connection " + super.getSocketClient().getInetAddress() + " ==========");
        super.getSockOut().println("++++++++++ Welcome to the amateur service ++++++++++");
        try {
            super.numActivityToLaunch();
        } catch (IOException e) {
            super.timeOutMsg();
        } finally {
            super.closeSocketClient();
        }
    }
}