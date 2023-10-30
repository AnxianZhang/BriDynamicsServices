package service;

import service.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public abstract class ServiceClient extends Service{
    private static List<Class<? extends Runnable>> servicesClasses;
    static {
        servicesClasses = new Vector<>();
    }

    public ServiceClient (Socket socketClient) throws IOException {
        super(socketClient);
    }

    public static void addService(Class<? extends Service> newServiceClass) {
        // vérifier la conformité par introspection
        try {
            validationBRI(newServiceClass);
            servicesClasses.add(newServiceClass);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // si non conforme --> exception avec message clair
        // si conforme, ajout au vector

    }

    // renvoie la classe de service (numService -1)
    public static Class<? extends Runnable> getServiceClass(int numService) {
        return servicesClasses.get(numService - 1);
    }

    public String toStringue() {
        StringBuilder r = new StringBuilder();
        r.append("Activités présentes :##");
        //no /n car va supprimer les text apres /n(socket not allow)
        int i= 1;
        synchronized (servicesClasses) {
            for (Class<? extends Runnable> s: servicesClasses) {
                try {
                    Method toStringue =s.getMethod("toStringue");
                    String ts = (String) toStringue.invoke(s);
                    r.append(i).append(" . ").append(ts).append("##");
                    ++i;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println(r.toString());
        return r.toString();
    }

    private static void validationBRI(Class<?> classe) throws Exception {
        if (!Modifier.isPublic(classe.getModifiers()))
            throw new Exception("La classe doit être publique");
        if (Modifier.isAbstract(classe.getModifiers()))
            throw new Exception("La classe ne doit pas être abstract");
        if (!Service.class.isAssignableFrom(classe))
            throw new Exception("La classe doit implémenter service.Service");
        try {
            Constructor<?> constructor = classe.getConstructor(Socket.class);
            if (!Modifier.isPublic(constructor.getModifiers()) || constructor.getExceptionTypes().length != 0) {
                throw new Exception("La classe doit posséder un constructeur public (Socket) sans exception");
            }
        } catch (Exception e) {
            throw new Exception("La classe doit posséder un constructeur public (Socket) sans exception");
        }
        Field[] fields = classe.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() != Socket.class||!Modifier.isPrivate(field.getModifiers()) || !Modifier.isFinal(field.getModifiers()))
                throw new Exception("La classe doit avoir un attribut Socket private final");
        }
        Method method = classe.getDeclaredMethod("toStringue");
        if (!Modifier.isPublic(method.getModifiers())||
                !Modifier.isStatic(method.getModifiers())||
                method.getReturnType() != String.class||
                method.getExceptionTypes().length != 0)
            throw new Exception("La classe doit avoir une méthode public static String toStringue() sans exception");
    }

    protected void closeSocketClient() {
        try {
            super.getSocketClient().close();
            System.out.println("========== Client disconnection " + super.getSocketClient().getInetAddress() + " ==========");
            System.out.println();
        } catch (IOException e) {
            System.out.println("Problem when closing socket in ServiceClient");;
        }
    }

    @Override
    protected Socket getSocketClient() {
        return super.getSocketClient();
    }

    @Override
    protected BufferedReader getSockIn() {
        return super.getSockIn();
    }

    @Override
    protected void println(String msg) {
        super.println(msg);
    }
}
