package service;

import person.Person;
import person.Programmer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ServiceRegistry {
    private static List<Person> programmers;
    private static ConcurrentHashMap<Person, List<Class <? extends Service>>> servicesClasses;


    static {
        programmers = new Vector<>();
        servicesClasses = new ConcurrentHashMap<>();
    }

    private static void isValid(Class<? extends Service> classToCheck) throws Exception {
        Class<?>[] listOfImplements = classToCheck.getInterfaces();

        // ===== Si implement bri.Service =====
        boolean isSerializable = false;
        for (Class<?> c : listOfImplements)
            if (c.getName().equals("bri.Service")) {
                isSerializable = true;
                break;
            }

        if (!isSerializable)
            throw new Exception("La class" + classToCheck.getName() + " implement pas bri.Service");

        // ===== Si pas abstract =====
        if (Modifier.isAbstract(classToCheck.getModifiers())) {
            throw new Exception("La class" + classToCheck.getName() + " ne doit pas etre abstract");
        }

        // ===== Si public =====
        if (!Modifier.isPublic(classToCheck.getModifiers()))
            throw new Exception("La class" + classToCheck.getName() + " doit etre public");

        // ===== si  constructeur public (Socket) sans exception =====
        if (classToCheck.getConstructor(Socket.class).getExceptionTypes().length != 0) {
            throw new Exception("La class" + classToCheck.getName() + " ne doit pas avoir d'exeption");
        }

        // ===== si un attribut Socket private final =====
        Field[] fileds = classToCheck.getDeclaredFields();

        for (Field f : fileds) {
            if (f.getType() != Socket.class || !Modifier.isPrivate(f.getModifiers()) || !Modifier.isFinal(f.getModifiers())) {
                throw new Exception("La class" + classToCheck.getName() + " doit avoir un attribut Socket private final");
            }
        }

        // ===== is une méthode public static String toStringue() sans exception =====
        try {
            Method m = classToCheck.getMethod("toStringue");
            if (m.getExceptionTypes().length != 0) {
                throw new Exception("La methode" + classToCheck.getName() + " ne doit pas avoir d'exeption");
            }

            if (m.getReturnType() != String.class) {
                throw new Exception("La methode" + classToCheck.getName() + " ne retourne pas le type String");
            }

            if (!Modifier.isPublic(m.getModifiers())) {
                throw new Exception("La methode" + classToCheck.getName() + " n'est pas publique");
            }

            if (!Modifier.isStatic(m.getModifiers())) {
                throw new Exception("La methode" + classToCheck.getName() + " n'est pas statique");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("La methode" + classToCheck.getName() + " toStringue n'existe pas");
        }
    }

//     ajoute une classe de service après contrôle de la norme BRi
    public static void addService(Programmer prog,  Class<? extends Service> classToCharge) {
        List<Class<? extends Service>> services = servicesClasses.get(prog);
        if (services == null) {
            services = new Vector<>();
            servicesClasses.put(prog, services);
        }
        try	{
            isValid(classToCharge);
            services.add(classToCharge);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // renvoie la classe de service (numService -1)
//    public static Class<? extends Service> getServiceClass(int numService){
//        return servicesClasses.get(numService -1);
//    }

    public static List<Class <? extends Service>> getAllServices(){
        List<Class <? extends Service>> allServiceClasses = servicesClasses.values().stream()
                .flatMap(List::stream).collect(Collectors.toList());
        return allServiceClasses;
    }

    public static void addProgrammer(String login, String pwd){
        programmers.add(new Programmer(login, pwd));
        servicesClasses.put(programmers.get(programmers.size() - 1), new Vector<>());
    }

    public static Person getProgrammer (String login, String pwd){
        for (Person p: programmers){
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
