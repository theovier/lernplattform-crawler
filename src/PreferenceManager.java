import java.nio.file.Path;
import java.util.prefs.Preferences;

public class PreferenceManager {


    public static void main (String[] args) {
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
        String newValue = "a string xoxo";
        prefs.put(PREF_NAME, newValue);
    }

    public static void setDirectory() {

    }

    public static void setUsername() {

    }

    public static void setLastUsedUsername() {

    }

    public static Path getDirectory() {
        return null;
    }

    public static String getUsername() {
        return null;
    }

    public static String getLastUsedUsername() {
        //idee
        //save lasttyped name wenn button gedrückt
        //wenn login erfolgreich dann lasttyped name gespeichert in username
        return null;
    }
}
