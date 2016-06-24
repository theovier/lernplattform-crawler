import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class DocumentProducer implements Runnable{

    private LinkedBlockingQueue<DownloadableDocument> queue;
    private WebClient browser;
    private HtmlPage overviewPage;
    private CourseCrawler courseCrawler;
    private GatewayCrawler gatewayCrawler;
    private DocumentCrawler documentCrawler;
    private int produced;
    private boolean running;

    public DocumentProducer(LinkedBlockingQueue queue, WebClient browser, HtmlPage overviewPage) {
        this.queue = queue;
        this.browser = browser;
        this.overviewPage = overviewPage;
        initDefaultValues();
        initCrawlers();
    }

    private void initDefaultValues() {
        running = true;
        produced = 0;
    }

    private void initCrawlers() {
        courseCrawler = new CourseCrawler();
        gatewayCrawler = new GatewayCrawler();
        documentCrawler = new DocumentCrawler();
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        produceDocuments();
        System.out.println("Producer Completed: " + produced);
        running = false;
    }

    private void produceDocuments() {
        try {
            crawlWebsite();
        } catch (IOException e) {
            System.err.println("Error beim Parsen der Webseiten!");
        }
    }

    //todo rename?
    private void crawlWebsite() throws IOException {
        List<String> courseLinks = courseCrawler.fetchCourseLinks(overviewPage);
        for (String link : courseLinks) {
            crawlCourse(link);
        }
    }

    //todo rename?
    private void crawlCourse(String courseLink) throws IOException {
        HtmlPage coursePage = browser.getPage(courseLink);
        List<String> downloadLinks = gatewayCrawler.fetchDownloadLinks(coursePage);
        for (String link : downloadLinks) {
            DownloadableDocument doc = fetchDocument(coursePage, link);
            enqueue(doc);
        }
    }

    private DownloadableDocument fetchDocument(HtmlPage coursePage, String downloadPageURL) throws IOException {
        String courseName = gatewayCrawler.fetchCourseName(coursePage);
        HtmlPage downloadPage = browser.getPage(downloadPageURL);
        return documentCrawler.getDocument(downloadPage, courseName);
    }

    private void enqueue(DownloadableDocument doc) {
        try {
            queue.put(doc);
            produced++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getProducedDocumentsAmount() {
        return produced;
    }
}


