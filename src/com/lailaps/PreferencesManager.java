package com.lailaps;

import java.util.prefs.Preferences;

public class PreferencesManager {

    private static PreferencesManager prefManager;
    private static Preferences prefs;
    private static final String PREF_DIR = "directory";
    private static final String PREF_USER = "user";
    private static final String PREF_LAST_USER = "lastUser";
    private static final String DEFAULT_DIR = "";
    private static final String DEFAULT_USER = "Vorname.Nachname";

    public static PreferencesManager getInstance() {
        if (prefManager == null)
            prefManager = new PreferencesManager();
        return prefManager;
    }

    private PreferencesManager() {
        prefs = Preferences.userNodeForPackage(PreferencesManager.class);
    }

    public static void setDirectory(String directory) {
        prefs.put(PREF_DIR, directory);
    }

    public static void setUsername(String username) {
        prefs.put(PREF_USER, username);
    }

    public static void setLastUsedUsername(String lastUsedUsername) {
        prefs.put(PREF_LAST_USER, lastUsedUsername);
    }

    public static String getDirectory() {
        return prefs.get(PREF_DIR, DEFAULT_DIR);
    }

    public static String getUsername() {
        return prefs.get(PREF_USER, DEFAULT_USER);
    }

    //todo rethink method?
    public static String getLastUsedUsername() {
        //idee
        //save lasttyped name wenn button gedrückt
        //wenn login erfolgreich dann lasttyped name gespeichert in username

        //btnLogin pressed -> setLastUsedUsername()
        //login erfolgreich -> setLastUser ( getLastUsedUsername() ) //evtl direkt eigene methode hier?
        return prefs.get(PREF_LAST_USER, DEFAULT_USER);
    }
}
