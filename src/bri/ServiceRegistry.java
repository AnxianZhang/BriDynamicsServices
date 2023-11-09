package bri;

import person.Person;
import person.Programmer;
import person.ProgrammerOfService;

import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ServiceRegistry {
    private static List<Programmer> programmers;
    private static ConcurrentHashMap<Person, List<Class<? extends Service>>> servicesClasses;


    static {
        programmers = new Vector<>();
        servicesClasses = new ConcurrentHashMap<>();
    }

    private static void isValid(Class<?> classe, Programmer p) throws Exception {
        if(!classe.getPackage().toString().equals("package " + p.getLogin()))
            throw new Exception("The class must be in a package with your login name");
        if (!Modifier.isPublic(classe.getModifiers()))
            throw new Exception("The class must be public");
        if (Modifier.isAbstract(classe.getModifiers()))
            throw new Exception("The class must not be abstract");
        if (!Service.class.isAssignableFrom(classe))
            throw new Exception("The class must implement bri.Service");
        try {
            Constructor<?> constructor = classe.getConstructor(Socket.class);
            if (!Modifier.isPublic(constructor.getModifiers()) || constructor.getExceptionTypes().length != 0) {
                throw new Exception("The class must have a public constructor without exception with a Socket parameter");
            }
        } catch (Exception e) {
            throw new Exception("The class must have a public constructor without exception with a Socket parameter");
        }
        Field[] fields = classe.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() != Socket.class||!Modifier.isPrivate(field.getModifiers()) || !Modifier.isFinal(field.getModifiers()))
                throw new Exception("The class must have a Socket attribute private final");
        }
        Method method = classe.getDeclaredMethod("toStringue");
        if (!Modifier.isPublic(method.getModifiers())||
                !Modifier.isStatic(method.getModifiers())||
                method.getReturnType() != String.class||
                method.getExceptionTypes().length != 0)
            throw new Exception("The class must have a public static String toStringue() method with no exceptions");
    }

    // ajoute une classe de service après contrôle de la norme BRi
    public static void addService(Class<?> classToCharge, Programmer p) throws Exception {
        // vérifier la conformité par introspection
        // si non conforme --> exception avec message clair
        // si conforme, ajout au vector
        isValid(classToCharge, p);
        if (!servicesClasses.get(p).contains(classToCharge.asSubclass(Service.class))){
            servicesClasses.get(p).add(classToCharge.asSubclass(Service.class));
            System.out.println("Class: " + classToCharge + " added by the programmer " + p.getLogin());
        }
    }
    
    // renvoie la classe de service (numService -1)
    public static Class<? extends Service> getServiceClass(int numService){
        return getAllServices().get(numService -1);
    }

    public static Programmer addProgrammer(String login, String pwd, String ftpUrl) throws MalformedURLException, Exception {
        Programmer p = new ProgrammerOfService(login, pwd, ftpUrl);

        if (programmers.contains(p))
            throw new Exception("This login is already used");

        programmers.add(p);
        servicesClasses.put(programmers.get(programmers.size() - 1), new Vector<>());

        return p;
    }

    public static Programmer getProgrammer (String login, String pwd){
        for (Programmer p: programmers){
            if (p.isSameLogin(login) && p.isSamePwd(pwd)){
                return p;
            }
        }
        return null;
    }

    public static List<Class<? extends Service>> getListServicesOfProg(Programmer p) throws Exception{
        if(!servicesClasses.get(p).isEmpty())
            return servicesClasses.get(p);
        throw new Exception("You don't have added any services, so you can't make update");
    }

    public static void updateService(Class<?> classToCharge, Programmer currentProgrammer) throws Exception {
        isValid(classToCharge, currentProgrammer);

        List<Class<? extends Service>> serviceList = getListServicesOfProg(currentProgrammer);

        for (int i = 0; i < serviceList.size(); ++i){
            if (serviceList.get(i).toString().equals(classToCharge.toString())){
                serviceList.set(i, classToCharge.asSubclass(Service.class));
                servicesClasses.put(currentProgrammer, serviceList);
                return;
            }
        }

        throw new Exception("This class isn't in the array");
    }

    public static List<Class <? extends Service>> getAllServices(){
        List<Class <? extends Service>> allServiceClasses = servicesClasses.values().stream()
                .flatMap(List::stream).collect(Collectors.toList());
        return allServiceClasses;
    }

    public static int getServicesSize(){
        return getAllServices() == null ? 0 : getAllServices().size();
    }

    // liste les activités présentes
    public static String toStringue() {
        StringBuilder result = new StringBuilder("Available activity:##");
        // todo
        int i = 1;
        synchronized (servicesClasses) {
            for (Class<? extends Service> c : getAllServices()) {
                try	{
					/*
					va chercher la méthode toStringue de la classe qui implement
					Service (ON VEUT LA CLASSE DE SERVICE). On ne fait pas c.toString car sinon il prendra la méthode
					tostring de la classe Class
					* */
                    Method toStringue = c.getMethod("toStringue");
                    String s = (String) toStringue.invoke(c); // on met "c" en param car c'est l'objet recepteur qui possède la méthode toStringue (on peut aussi passer des param, mais la on ne le fait pas car toStringue na pas de param)
                    result.append(i).append(". ").append(s).append("##");
                    ++i;
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return result.toString();
    }

}
