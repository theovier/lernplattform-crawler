import java.nio.file.Path;
import java.util.prefs.Preferences;

public class PreferenceManager {

    private static PreferenceManager prefManager;
    private static Preferences prefs;
    private static final String PREF_DIR = "directory";
    private static final String PREF_USER = "user";
    private static final String PREF_LAST_USER = "lastUser";
    private static final String DEFAULT_DIR = ".";
    private static final String DEFAULT_USER = "Vorname.Nachname";

    public static PreferenceManager getInstance() {
        if (prefManager == null)
            prefManager = new PreferenceManager();
        return prefManager;
    }

    private PreferenceManager() {
        prefs = Preferences.userNodeForPackage(PreferenceManager.class);
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

    //todo fetch instead of get? save instead of set?

    public static void main (String[] args) {
        PreferenceManager pm = PreferenceManager.getInstance();

        System.out.println(pm.getUsername());
        pm.setUsername("hans");


        /*
        //todo check error
        //WARNING: Could not open/create prefs root node Software\JavaSoft\Prefs at root 0x80000002.
        //Windows RegCreateKeyEx(...) returned error code 5.
        //klappt aber trotzdem?

        //todo causes error, keine admin rechte
        // Retrieve the user preference node for the package com.mycompany
        Preferences prefs = Preferences.userNodeForPackage(PreferenceManager.class);

        // Preference key name
        final String PREF_NAME = "name_of_preference";

        // Get the value of the preference;
        // default value is returned if the preference does not exist
        String defaultValue = "default string";
        String propertyValue = prefs.get(PREF_NAME, defaultValue); // "a string"
        System.out.println(propertyValue);

        // Set the value of the preference
        String newValue = "xoxo string <3";
        prefs.put(PREF_NAME, newValue);
        */
    }
}
