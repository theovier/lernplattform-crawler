package com.lailaps.crawler;


import com.gargoylesoftware.htmlunit.html.HtmlLabel;

public class Term {

    private String name, directoryFriendlyName;

    public Term (HtmlLabel termLabel) {
        this.name = termLabel.getAttribute("title").toString();
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
