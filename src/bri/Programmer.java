package bri;

import java.net.MalformedURLException;

public interface Programmer extends Person{
    void setFtpUrl(String newFtpUl);
    Class<?> laodClass(String className) throws ClassNotFoundException, MalformedURLException;
}