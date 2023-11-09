package person;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

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

    private void refreshURLClassLoader() throws MalformedURLException {
        this.serviceLoader = URLClassLoader.newInstance(new URL[] { new URL(this.ftpUrl)});
    }

    public void setFtpUrl(String newFtpUl){
        this.ftpUrl = newFtpUl;
        try{
            refreshURLClassLoader();
            System.out.println(this.serviceLoader);
        } catch (MalformedURLException e){
            /* tested in ServiceForProgrammer */
        }
    }

    public Class<?> laodClass(String className) throws ClassNotFoundException, MalformedURLException {
        this.refreshURLClassLoader();
        return this.serviceLoader.loadClass(className);
    }

    @Override
    public boolean isSamePwd(String pdwIn){
        return this.hashedPwd.equals(HashPassword.getHashPassword(pdwIn, this.salt));
    }

    @Override
    public String getLogin() {
        return this.login;
    }

    @Override
    public boolean isSameLogin(String login){
        return this.login.equals(login);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgrammerOfService that = (ProgrammerOfService) o;
        return Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}