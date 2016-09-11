package com.lailaps;

import com.lailaps.ui.LoginWindow;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args ) {
        LoginWindow window = new LoginWindow();
        window.show();
    }

//http://stackoverflow.com/questions/9689793/cant-execute-jar-file-no-main-manifest-attribute
    //todo wait logo while logging in and downloading
}

