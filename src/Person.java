public abstract class Person {
    private String login;
    private final String hashedPwd;

    private final byte [] salt;

    protected Person (String login, String pwd){
        this.login = login;
        this.salt = HashPassword.generateSalt();
        this.hashedPwd = HashPassword.getHashPassword(pwd, this.salt);
    }

    public String getHashedPwd() {
        return hashedPwd;
    }

    public boolean isSamePwd(String pdwIn){
        return this.hashedPwd.equals(HashPassword.getHashPassword(pdwIn, this.salt));
    }
}
