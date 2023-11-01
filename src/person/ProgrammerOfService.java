package person;

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