package bri.service.service_action;

import bri.ServiceRegistry;
import bri.Programmer;

public class AddServiceAction extends ServiceActionTemplate{

    @Override
    protected String promptMessage(Programmer p) {
        return "Enter the service you want to add: ";
    }

    @Override
    protected void handleAction(Class<?> c, Programmer p) throws Exception {
        ServiceRegistry.addService(c, p);
    }

    @Override
    protected String serviceActionMessage(Class<?> c) {
        return c + " is now added##Press a key to continue##";
    }
}
