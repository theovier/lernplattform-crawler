package com.lailaps.download;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.lailaps.Browser;
import com.lailaps.crawler.CourseCrawler;
import com.lailaps.crawler.DocumentCrawler;
import com.lailaps.crawler.GatewayCrawler;
import com.lailaps.crawler.TermCrawler;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class DocumentProducer implements Runnable{

    private static final Logger LOG = Logger.getLogger(DocumentProducer.class);
    private LinkedBlockingQueue<DownloadableDocument> queue;
    private Browser browser;
    private HtmlPage overviewPage;
    private CourseCrawler courseCrawler;
    private GatewayCrawler gatewayCrawler;
    private DocumentCrawler documentCrawler;
    private TermCrawler termCrawler;
    private int produced = 0;
    private boolean running;

    public DocumentProducer(LinkedBlockingQueue queue, Browser browser) {
        this.queue = queue;
        this.browser = browser;
        this.running = true;
        this.overviewPage = browser.getCurrentPage();
        initCrawlers();
    }

    private void initCrawlers() {
        termCrawler = new TermCrawler();
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
        LOG.info("Producer Completed: " + produced);
        running = false;
    }

    private void produceDocuments() {
        try {
            crawlWebsite();
        } catch (IOException e) {
            LOG.error("Error beim Parsen der Webseiten!");
        }
    }

    //todo rename
    private void crawlWebsite() throws IOException {
        String currentTerm = crawlTerm();
        List<String> courseLinks = courseCrawler.fetchCourseLinks(overviewPage, currentTerm);
        for (String link : courseLinks) {
            crawlCourse(link);
        }
    }

    private String crawlTerm() {
        String term = termCrawler.fetchCurrentTerm(overviewPage);
        String termDisplayName = termCrawler.clearTerm(term);
        Downloader.changeRootDir(termDisplayName);
        return term;
    }

    //todo rename, more than 1 effect.
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


