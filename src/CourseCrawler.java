import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAnchorElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement;
import org.jcp.xml.dsig.internal.dom.DOMXMLObject;

import java.util.ArrayList;
import java.util.List;

public class CourseCrawler extends Crawler {

    private String semesteryear;

    public CourseCrawler(String semester, String year) {
       semester = semester + " " + year;
    }

    //todo refactor
    public List<String> fetchCourseLinks(HtmlPage overviewPage) {
        List<String> links = new ArrayList<String>();
        HtmlLabel kurslisteLabel = (HtmlLabel) overviewPage.getFirstByXPath("//label[@title='Sommersemester 2016']");
        HtmlOrderedList courseList = (HtmlOrderedList) kurslisteLabel.getNextElementSibling().getNextElementSibling();
        courseList.getElementsByTagName("a").forEach(course -> {
            HtmlAnchor anchor = (HtmlAnchor) course;
            links.add(anchor.getHrefAttribute());
        });
        return links;
    }
}
