package com.lailaps.crawler;

import com.gargoylesoftware.htmlunit.html.*;

import java.util.ArrayList;
import java.util.List;

public class CourseCrawler {

    public static List<String> fetchCourseLinks(HtmlPage overviewPage, Term term) {
        List<String> links = new ArrayList<>();
        List<HtmlElement> courses = fetchCourses(overviewPage, term);
        courses.forEach(course -> {
            HtmlAnchor anchor = (HtmlAnchor) course;
            links.add(anchor.getHrefAttribute());
        });
        return links;
    }

    private static List<HtmlElement> fetchCourses(HtmlPage overviewPage, Term term) {
        HtmlLabel courseListLabel = overviewPage.getFirstByXPath("//label[@title='" + term + "']");
        HtmlOrderedList courseList = (HtmlOrderedList) courseListLabel.getNextElementSibling().getNextElementSibling(); //todo refactor
        return courseList.getElementsByTagName("a");
    }
}
