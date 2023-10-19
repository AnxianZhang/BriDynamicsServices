import java.util.List;
import java.util.Vector;

public class ServiceAmateurs {
    private static List<Class<? extends Service>> servicesClasses;
    static {
        servicesClasses = new Vector<>();
    }

    public static String toStringue() {
        return null;
        //class data
    }

    public static Class<? extends Service> getServiceClass(int numService) {
        return servicesClasses.get(numService - 1);
    }
}
