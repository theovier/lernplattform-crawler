import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFGatewayCrawler extends Crawler {

    private static final String RESOURCEPATH = "https://campusapp01.hshl.de/mod/resource/view.php?id=";

    public PDFGatewayCrawler(WebClient browser, HtmlPage currentPage) {
        super(browser, currentPage);
    }

    public void startDemo() {
        String backendPage = "https://campusapp01.hshl.de/course/view.php?id=3072";
        try {
            for (String s :  fetchPDFGateLinks(browser.getPage(backendPage))) {
                System.out.println(s);
            }
            currentPage = browser.getPage("https://campusapp01.hshl.de/mod/resource/view.php?id=86171");
            PDFCrawler c = new PDFCrawler(browser, currentPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> fetchPDFGateLinks(HtmlPage coursePage) {
        List<String> downloadLinks = new ArrayList<String>();
        List<String> resourceIDs = fetchResourceIDs(coursePage);
        resourceIDs.forEach(id -> downloadLinks.add(RESOURCEPATH + id));
        return downloadLinks;
    }

    private List<String> fetchResourceIDs(HtmlPage coursePage) {
        List<String> courseIDs = new ArrayList<String>();
        List<?> courseListItems = coursePage.getByXPath("//li[contains(@class, 'activity resource modtype_resource')]");
        getIDs(courseListItems).forEach(id -> courseIDs.add(id));
        return courseIDs;
    }

    private List<String> getIDs(List<?> uncastedListItems) {
        List <String> ids = new ArrayList<String>();
        uncastedListItems.forEach(item -> {
            HtmlListItem listItem = (HtmlListItem) item;
            String numericID = getNumericID(listItem.getId());
            ids.add(numericID);
        });
        return ids;
    }

    private String getNumericID(String ID) {
        return ID.split("-")[1];
    }
}
