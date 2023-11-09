package person;

import bri.Service;

public interface Programmer extends Person{
    void setFtpUrl(String newFtpUl);
    Class<?> laodClass(String className) throws ClassNotFoundException;
}