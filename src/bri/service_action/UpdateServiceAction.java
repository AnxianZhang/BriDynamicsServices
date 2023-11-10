package bri.service_action;

import bri.ServiceRegistry;
import bri.Programmer;

public class UpdateServiceAction extends ServiceActionTemplate{

    @Override
    protected String promptMessage(Programmer p) throws Exception {
        return ServiceRegistry.getListServicesOfProg(p).toString() +
                "##Enter the class name of service you want to update: ";
    }

    @Override
    protected void handleAction(Class<?> c, Programmer p) throws Exception {
        ServiceRegistry.updateService(c, p);
    }

    @Override
    protected String serviceActionMessage(Class<?> c) {
        return c +  " updated##Press a key to continue##";
    }
}
