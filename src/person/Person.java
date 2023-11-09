package person;

public interface Person {
    boolean isSameLogin(String login);

    boolean isSamePwd(String pdwIn);

    String getLogin();
}
