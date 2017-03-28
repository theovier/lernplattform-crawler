package com.lailaps;

import java.util.prefs.Preferences;

public class PreferencesManager {

    private static Preferences prefs = Preferences.userNodeForPackage(PreferencesManager.class);
    private static final String PREF_DIR = "directory";
    private static final String PREF_USER = "";
    private static final String DEFAULT_DIR = System.getProperty("user.home");
    private static final String DEFAULT_USER = "";

    public static void setDirectory(String directory) {
        prefs.put(PREF_DIR, directory);
    }

    public static void setUsername(String username) {
        prefs.put(PREF_USER, username);
    }

    public static String getDirectory() {
        return prefs.get(PREF_DIR, DEFAULT_DIR);
    }

    public static String getUsername() {
        return prefs.get(PREF_USER, DEFAULT_USER);
    }

    public static boolean hasUsernamePreference() {
        String username = getUsername();
        if (DEFAULT_USER.equals(username)) return false;
        return true;
    }
}
