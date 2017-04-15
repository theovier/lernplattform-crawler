package com.lailaps.login;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ResourceBundle;

public class LoginErrorInterpreter {

    private static final Logger LOG = Logger.getLogger(LoginErrorInterpreter.class);

    public static String getErrorMsg(Exception e, ResourceBundle bundle) {
        LOG.debug(e);
        if (e instanceof WrongCredentialsException) {
            return bundle.getString("login.error.credentials");
        } else if (e instanceof IOException) {
            return bundle.getString("login.error.io");
        } else {
            return bundle.getString("login.error.default");
        }
    }

    public static String getErrorSubtitle(Exception e, ResourceBundle bundle) {
        if (e instanceof WrongCredentialsException) {
            return bundle.getString("login.error.credentials.subtitle");
        } else if (e instanceof IOException) {
            return bundle.getString("login.error.io.subtitle");
        } else {
            return bundle.getString(e.getMessage());
        }
    }
}
