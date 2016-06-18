
public class LoginCredentials {

    private String user;
    private String emailExtension;
    private String password;

    public LoginCredentials(String user, String emailExtension, String password) {
        this.user = user;
        this.emailExtension = emailExtension;
        this.password = password;

        System.out.println(user + emailExtension + " pw: " + password);
    }

    public String getUser() {
        return user;
    }

    public String getEmailExtension() {
        return emailExtension;
    }

    public String getPassword() {
        return password;
    }
}
