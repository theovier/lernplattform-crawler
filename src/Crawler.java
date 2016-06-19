import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Crawler {

    protected WebClient browser;
    protected HtmlPage currentPage;

    public Crawler(WebClient browser, HtmlPage currentPage) {
        this.browser = browser;
        this.currentPage = currentPage;
    }
}
