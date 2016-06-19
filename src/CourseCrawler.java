import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

public class CourseCrawler extends Crawler {

    private static final String COURSEPATH = "https://campusapp01.hshl.de/course/view.php?id=";

    public CourseCrawler(WebClient browser, HtmlPage currentPage) {
        super(browser, currentPage);
    }

    public List<String> fetchCourseLinks() {
        List<String> links = new ArrayList<String>();
        List<String> courseIDs = fetchCourseIDs();
        courseIDs.forEach(id -> links.add(COURSEPATH + id));
        return links;
    }

    private List<String> fetchCourseIDs() {
        List<String> ids = new ArrayList<String>();
        ids.add("3072");
        ids.add("3075");
        ids.add("3076");
        return ids;
    }

    //get label mit inhalt "Sommersemester 2016" (Sommersemester/Wintersemester XXXX)
    //die nächste ol -> davon die lis -> a  href
}
