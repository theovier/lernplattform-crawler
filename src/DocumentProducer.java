import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class DocumentProducer implements Runnable{

    private LinkedBlockingQueue<PDFDocument> queue;
    private CourseCrawler courseCrawler;
    private PDFGatewayCrawler gatewayCrawler;
    private PDFCrawler pdfCrawler;
    private WebClient browser;
    private HtmlPage overviewPage;
    private int uploadedDocuments;

    private boolean running;

    public DocumentProducer(LinkedBlockingQueue queue, WebClient browser, HtmlPage overviewPage) {
        this.queue = queue;
        this.browser = browser;
        this.overviewPage = overviewPage;
        courseCrawler = new CourseCrawler();
        gatewayCrawler = new PDFGatewayCrawler();
        pdfCrawler = new PDFCrawler();
        running = true;
        uploadedDocuments = 0;
    }

    public boolean isRunning() {
        return running;
    }

    public int getUploadedDocumentsAmount() {
        return uploadedDocuments;
    }

    @Override
    public void run() {

        try {
            for (String courseLink : courseCrawler.fetchCourseLinks(overviewPage)) {
                HtmlPage page = browser.getPage(courseLink);
                for (String gatewayLink : gatewayCrawler.fetchPDFGateLinks(page)) {
                    String courseName = gatewayCrawler.fetchCourseName(page);
                    page = browser.getPage(gatewayLink);
                    try {
                        PDFDocument pdf = pdfCrawler.getPDFDocument(page, courseName);
                        queue.put(pdf);
                        uploadedDocuments++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("error beim downloaden");
        }
        System.out.println("P Completed: " + uploadedDocuments);
        running = false;
    }
}


