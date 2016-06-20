import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class PDFCrawler extends Crawler {

    public static final String DOWNLOAD_START = "window.open('";
    public static final String DOWNLOAD_END = "\',";
    public static final String FILENAME_XPATH = "//div[@role='main']//h2";

    //todo: exception
    private String fetchFileName(HtmlPage currentPage) {
        HtmlHeading2 filename = (HtmlHeading2) currentPage.getFirstByXPath(FILENAME_XPATH);
        return clearName(filename.asText());
    }

    //todo regex
    private String clearName(String filename) {
        String clearedName =  filename.replace('/', '&');
        clearedName = clearedName.replace(':', ';');
        return clearedName;
    }

    //todo: exception
    private String fetchDownloadLink(HtmlPage currentPage) {
        String source = currentPage.asXml();
        int begin = source.indexOf(DOWNLOAD_START);
        int end = source.indexOf(DOWNLOAD_END, begin);
        return source.substring(begin + DOWNLOAD_START.length(), end);
    }

    public PDFDocument getPDFDocument(HtmlPage gatewayPage, String courseName) {
        return new PDFDocument(fetchFileName(gatewayPage), fetchDownloadLink(gatewayPage), courseName);
    }
}
