import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class PDFCrawler extends Crawler {

    public static final String DOWNLOAD_START = "window.open('";
    public static final String DOWNLOAD_END = "\',";
    public static final String FILENAME_XPATH = "//div[@role='main']//h2";

    public PDFCrawler(WebClient browser, HtmlPage currentPage) {
        super(browser, currentPage);
    }

    //todo: exception
    private String fetchFileName() {
        HtmlHeading2 filename = (HtmlHeading2) currentPage.getFirstByXPath(FILENAME_XPATH);
        return filename.asText();
    }

    //todo: exception
    private String fetchDownloadLink() {
        String source = currentPage.asXml();
        int begin = source.indexOf(DOWNLOAD_START);
        int end = source.indexOf(DOWNLOAD_END, begin);
        return source.substring(begin + DOWNLOAD_START.length(), end);
    }

    public PDFDocument getPDFDocument() {
        return new PDFDocument(fetchFileName(), fetchDownloadLink());
    }
}
