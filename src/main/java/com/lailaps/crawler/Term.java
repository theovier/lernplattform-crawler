package com.lailaps.crawler;


import com.gargoylesoftware.htmlunit.html.HtmlLabel;

public class Term implements Comparable<Term>{

    public static final String SUMMER_TERM = "Sommersemester";
    public static final String WINTER_TERM = "Wintersemester";

    private final String name; //e.g. "Sommersemester 2017" or "Wintersemester 2014/15"
    private final String directoryFriendlyName;
    private final int year; //the starting year. for 2015/16 it's 2015.


    public Term (HtmlLabel termLabel) {
        this.name = termLabel.getAttribute("title");
        this.directoryFriendlyName = clearName(name);
        this.year = determineYear(directoryFriendlyName);
    }

    private String clearName(String term) {
        return term.replace("/", "-");
    }

    private int determineYear(String name) {
        String year = name.split(" ")[1];
        year = year.substring(0, 4);
        return Integer.valueOf(year);
    }

    public String getDirectoryFriendlyName() {
        return directoryFriendlyName;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Term other) {
        //e.g. Sommersemester 2015 < Wintersemester 2015
        int otherYear = other.year;
        if (year > otherYear) {
            return 1;
        } else if (year < otherYear) {
            return -1;
        } else {
            if (name.contains(WINTER_TERM) && other.name.contains(SUMMER_TERM)) {
                return 1;
            } else if (name.contains(SUMMER_TERM) && other.name.contains(WINTER_TERM)) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
