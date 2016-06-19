import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

public class CourseCrawler extends Crawler {

    private static final String COURSEPATH = "https://campusapp01.hshl.de/course/view.php?id=";

    public CourseCrawler() {

    }

    public List<String> fetchCourseLinks(HtmlPage overviewPage) {
        List<String> links = new ArrayList<String>();
        List<String> courseIDs = fetchCourseIDs();
        courseIDs.forEach(id -> links.add(COURSEPATH + id));
        return links;
    }

    private List<String> fetchCourseIDs() {
        List<String> ids = new ArrayList<String>();

        //label mit inhalt "Sommersemester 2016"
        //die nächste ol -> davon die li -> a  href

        ids.add("3072");
        ids.add("3075");
        ids.add("3076");
        return ids;
    }
}
