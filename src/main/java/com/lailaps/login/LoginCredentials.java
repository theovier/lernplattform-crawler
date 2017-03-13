package com.lailaps.login;

public class LoginCredentials {

    private String user;
    private String emailExtension;
    private String password;

    public LoginCredentials(String user, String emailExtension, String password) {
        this.user = user;
        this.emailExtension = emailExtension;
        this.password = password;
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

    public String toString() {
        return "user: " + this.user + "; password: " + this.password;
    }
}
