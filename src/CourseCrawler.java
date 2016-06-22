import com.gargoylesoftware.htmlunit.html.*;

import java.util.ArrayList;
import java.util.List;

public class CourseCrawler extends Crawler {

    private String semesteryear;

    public CourseCrawler(String semester, String year) {
        semesteryear = semester + " " + year;
    }

    public List<String> fetchCourseLinks(HtmlPage overviewPage) {
        List<String> links = new ArrayList<>();
        fetchCourses(overviewPage).forEach(course -> {
            HtmlAnchor anchor = (HtmlAnchor) course;
            links.add(anchor.getHrefAttribute());
        });
        return links;
    }

    private List<HtmlElement> fetchCourses(HtmlPage overviewPage) {
        HtmlLabel courseListLabel = overviewPage.getFirstByXPath("//label[@title='Sommersemester 2016']");
        HtmlOrderedList courseList = (HtmlOrderedList) courseListLabel.getNextElementSibling().getNextElementSibling();
        return courseList.getElementsByTagName("a");
    }
}
