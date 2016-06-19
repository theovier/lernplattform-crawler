import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CourseCrawler extends Crawler {

    public CourseCrawler(WebClient browser, HtmlPage currentPage) {
        super(browser, currentPage);
    }

    private void fetchCourses(HtmlPage overviewPage) {
        //return list
    }
}
