package com.lailaps.crawler;


public class Term {

    private String name, directoryFriendlyName;

    public Term(String name) {
        this.name = name;
        this.directoryFriendlyName = clearName(name);
    }

    private String clearName(String term) {
        return term.replace("/", "-");
    }

    public String getDirectoryFriendlyName() {
        return directoryFriendlyName;
    }

    @Override
    public String toString() {
        return name;
    }
}
