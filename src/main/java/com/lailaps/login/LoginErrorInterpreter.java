package com.lailaps.login;


import org.apache.log4j.Logger;

import java.io.IOException;

public class LoginErrorInterpreter {

    private static final Logger LOG = Logger.getLogger(LoginErrorInterpreter.class);
    private static final String MSG_WRONG_CREDENTIALS = "wrong login credentials";
    private static final String MSG_CONNECTION_PROBLEMS = "connection problems";
    private static final String MSG_DEFAULT = "an error occurred";

    public static String getErrorMsg(Exception e) {
        LOG.debug(e);
        if (e instanceof WrongCredentialsException) {
            return MSG_WRONG_CREDENTIALS;
        } else if (e instanceof IOException) {
            return MSG_CONNECTION_PROBLEMS;
        } else {
            return MSG_DEFAULT + " " + e.getMessage();
        }
    }
}
