package com.lailaps.crawler;

import com.gargoylesoftware.htmlunit.html.*;

import java.util.ArrayList;
import java.util.List;

public class CourseCrawler extends Crawler {

    private String term;

    public CourseCrawler(String term) {
        this.term = term;
    }

    public List<String> fetchCourseLinks(HtmlPage overviewPage) {
        List<String> links = new ArrayList<>();
        List<HtmlElement> courses = fetchCourses(overviewPage);
        courses.forEach(course -> {
            HtmlAnchor anchor = (HtmlAnchor) course;
            links.add(anchor.getHrefAttribute());
        });
        return links;
    }

    private List<HtmlElement> fetchCourses(HtmlPage overviewPage) {
        HtmlLabel courseListLabel = overviewPage.getFirstByXPath("//label[@title='" + term + "']");
        HtmlOrderedList courseList = (HtmlOrderedList) courseListLabel.getNextElementSibling().getNextElementSibling();
        return courseList.getElementsByTagName("a");
    }
}
