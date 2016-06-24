import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class DocumentCrawler extends Crawler {

    public static final String DOWNLOAD_START = "window.open('";
    public static final String DOWNLOAD_END = "\',";
    public static final String FILENAME_XPATH = "//div[@role='main']//h2";

    private String fetchFileName(HtmlPage currentPage) {
        HtmlHeading2 filename = currentPage.getFirstByXPath(FILENAME_XPATH);
        return clearName(filename.asText());
    }

    //todo regex
    private String clearName(String filename) {
        String clearedName =  filename.replace('/', '&');
        clearedName = clearedName.replace(':', ';');
        return clearedName;
    }

    private String fetchDownloadLink(HtmlPage currentPage) {
        String source = currentPage.asXml();
        int begin = source.indexOf(DOWNLOAD_START);
        int end = source.indexOf(DOWNLOAD_END, begin);
        return source.substring(begin + DOWNLOAD_START.length(), end);
    }

    //todo
    private String fetchFileExtension(String link) {
        return ".pdf";
    }

    public DownloadableDocument getDocument(HtmlPage gatewayPage, String courseName) {
        return new DownloadableDocument(fetchFileName(gatewayPage), fetchDownloadLink(gatewayPage), courseName, fetchFileExtension(""));
    }
}
