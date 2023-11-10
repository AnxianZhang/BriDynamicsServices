package bri;

import java.net.MalformedURLException;

public interface Programmer extends Person{
    void setFtpUrl(String newFtpUl);
    Class<?> loadClass(String className) throws ClassNotFoundException, MalformedURLException;

    boolean containClassToCharge(Class<? extends Service> myClass);
}