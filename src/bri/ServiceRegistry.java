package bri;

import person.Person;
import person.Programmer;
import person.ProgrammerOfService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {
    private static List<Programmer> programmers;
    private static ConcurrentHashMap<Person, List<Class<? extends Service>>> servicesClasses;


    static {
        programmers = new Vector<>();
        servicesClasses = new ConcurrentHashMap<>();
    }

    private static void isValid(Class<?> classe) throws Exception {
        if (!Modifier.isPublic(classe.getModifiers()))
            throw new Exception("La classe doit être publique");
        if (Modifier.isAbstract(classe.getModifiers()))
            throw new Exception("La classe ne doit pas être abstract");
        if (!Service.class.isAssignableFrom(classe))
            throw new Exception("La classe doit implémenter bri.Service");
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

    // ajoute une classe de service après contrôle de la norme BRi
    public static void addService(Class<?> classToCharge, Programmer p) {
        // vérifier la conformité par introspection
        // si non conforme --> exception avec message clair
        // si conforme, ajout au vector
        try	{
            isValid(classToCharge);
            if (!servicesClasses.get(p).contains(classToCharge.asSubclass(Service.class))){
                servicesClasses.get(p).add(classToCharge.asSubclass(Service.class));
                System.out.println("Ajout de la classe: " + classToCharge);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    // renvoie la classe de service (numService -1)
//    public static Class<? extends Service> getServiceClass(int numService){
//        return servicesClasses.get(numService -1);
//    }

    public static Programmer addProgrammer(String login, String pwd, String ftpUrl) throws MalformedURLException {
        Programmer p = new ProgrammerOfService(login, pwd, ftpUrl);

        programmers.add(p);
        servicesClasses.put(programmers.get(programmers.size() - 1), new Vector<>());

        return p;
    }

    public static Programmer getProgrammer (String login, String pwd){
        for (Programmer p: programmers){
            if (p.isSameLogin(login) && p.isSameLogin(pwd)){
                return p;
            }
        }
        return null;
    }

    // liste les activités présentes
//    public static String toStringue() {
//        StringBuilder result = new StringBuilder("Activités présentes :##");
//        // todo
//        int i = 1;
//        synchronized (servicesClasses) {
//            for (Class<? extends Service> c : servicesClasses) {
//                try	{
//					/*
//					va chercher la méthode toStringue de la classe qui implement
//					Service (ON VEUT LA CLASSE DE SERVICE). On ne fait pas c.toString car sinon il prendra la méthode
//					tostring de la classe Class
//					* */
//                    Method toStringue = c.getMethod("toStringue");
//                    String s = (String) toStringue.invoke(c); // on met "c" en param car c'est l'objet recepteur qui possède la méthode toStringue (on peut aussi passer des param, mais la on ne le fait pas car toStringue na pas de param)
//                    result.append(i).append(s).append("##");
//                    ++i;
//                } catch (InvocationTargetException e) {
//                    throw new RuntimeException(e);
//                } catch (NoSuchMethodException e) {
//                    throw new RuntimeException(e);
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//        }
//        return result.toString();
//    }

}
