package com.lailaps.crawler;

import com.gargoylesoftware.htmlunit.html.*;

import java.util.ArrayList;
import java.util.List;

public class CourseCrawler extends Crawler {

    public List<String> fetchCourseLinks(HtmlPage overviewPage, String term) {
        List<String> links = new ArrayList<>();
        List<HtmlElement> courses = fetchCourses(overviewPage, term);
        courses.forEach(course -> {
            HtmlAnchor anchor = (HtmlAnchor) course;
            links.add(anchor.getHrefAttribute());
        });
        return links;
    }

    private List<HtmlElement> fetchCourses(HtmlPage overviewPage, String term) {
        HtmlLabel courseListLabel = overviewPage.getFirstByXPath("//label[@title='" + term + "']");
        HtmlOrderedList courseList = (HtmlOrderedList) courseListLabel.getNextElementSibling().getNextElementSibling(); //todo refactor
        return courseList.getElementsByTagName("a");
    }
}
