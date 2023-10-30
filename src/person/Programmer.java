package person;

public class Programmer implements Person{
    private String login;
    private final String hashedPwd;

    private final byte [] salt;

    public Programmer(String login, String pwd) {
        this.login = login;
        this.salt = HashPassword.generateSalt();
        this.hashedPwd = HashPassword.getHashPassword(pwd, this.salt);
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