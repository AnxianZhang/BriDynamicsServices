package person;

import bri.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ProgrammerOfService implements Programmer{
    private String login;
    private final String hashedPwd;
    private String ftpUrl;

    private URLClassLoader serviceLoader;

    private final byte [] salt;

    public ProgrammerOfService(String login, String pwd, String ftpUrl) throws MalformedURLException {
        this.login = login;
        this.salt = HashPassword.generateSalt();
        this.hashedPwd = HashPassword.getHashPassword(pwd, this.salt);
        this.ftpUrl = ftpUrl;
        this.serviceLoader = URLClassLoader.newInstance(new URL[] { new URL(ftpUrl)});

//        try {
//            // ?
//            System.out.println(this.serviceLoader.loadClass("examples.Bonjour"));
//
//            // ???
//            System.out.println(this.serviceLoader.loadClass("examples.ServiceSese"));
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    public boolean isFtpUrlCorrect (String ftpUrl){
        try {
            this.ftpUrl = ftpUrl;
            this.serviceLoader = URLClassLoader.newInstance(new URL[] { new URL(ftpUrl)});
            return true;
        } catch (MalformedURLException e){
            return false;
        }
    }

    public void setFtpUrl(String newFtpUl){
        this.ftpUrl = newFtpUl;
        try{
            this.serviceLoader = URLClassLoader.newInstance(new URL[] { new URL(this.ftpUrl)});
            System.out.println(this.serviceLoader);
        } catch (MalformedURLException e){
            /* tested in ServiceForProgrammer */
        }
    }

    public Class<?> laodClass(String className) throws ClassNotFoundException {
        return this.serviceLoader.loadClass(className);
    }

    public String getHashedPwd() {
        return hashedPwd;
    }

    @Override
    public boolean isSamePwd(String pdwIn){
        return this.hashedPwd.equals(HashPassword.getHashPassword(pdwIn, this.salt));
    }

    @Override
    public boolean isSameLogin(String login){
        return this.login.equals(login);
    }
}