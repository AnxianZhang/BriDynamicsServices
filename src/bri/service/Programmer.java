package bri.service;

import bri.Person;

import java.net.MalformedURLException;

public interface Programmer extends Person {
    void setFtpUrl(String newFtpUl);

    Class<?> loadClass(String className) throws ClassNotFoundException, MalformedURLException;
}