package com.lailaps.download;

import com.gargoylesoftware.htmlunit.Page;
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

public class DocumentProducer implements Runnable {

    private static final Logger LOG = Logger.getLogger(DocumentProducer.class);
    private LinkedBlockingQueue<DownloadableDocument> queue;
    private Downloader downloader;
    private Browser browser;
    private HtmlPage overviewPage;
    private TermCrawler termCrawler = new TermCrawler();
    private CourseCrawler courseCrawler = new CourseCrawler();
    private GatewayCrawler gatewayCrawler = new GatewayCrawler();
    private DocumentCrawler documentCrawler = new DocumentCrawler();
    private int produced = 0;
    private boolean running;

    public DocumentProducer(LinkedBlockingQueue queue, Downloader downloader, Browser browser) {
        this.queue = queue;
        this.downloader = downloader;
        this.browser = browser;
        this.running = true;
        this.overviewPage = browser.getCurrentPage();
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        produceDocuments();
        LOG.info("Producer Completed: " + produced);
        if (produced == 0) {
            downloader.onNoFilesToDownload();
        }
        running = false;
    }

    private void produceDocuments() {
        String currentTerm = termCrawler.fetchCurrentTerm(overviewPage);
        notifyDownloaderAboutTerm();
        fetchDocumentsForTerm(currentTerm);
    }

    private void notifyDownloaderAboutTerm() {
        String directoryFriendlyTerm = termCrawler.getDirectoryFriendlyTerm(overviewPage);
        downloader.changeRootDir(directoryFriendlyTerm);
    }

    private void fetchDocumentsForTerm(String currentTerm)  {
        List<String> courseURLs = courseCrawler.fetchCourseLinks(overviewPage, currentTerm);
        for (String courseURL : courseURLs) {
            fetchDocumentsForCourse(courseURL);
        }
    }

    private void fetchDocumentsForCourse(String courseURL) {
        try {
            HtmlPage coursePage = browser.getPage(courseURL);
            List<String> downloadGatewayURLs = gatewayCrawler.fetchDownloadLinks(coursePage);
            for (String downloadGatewayURL : downloadGatewayURLs) {
                DownloadableDocument doc = createDocument(downloadGatewayURL, coursePage);
                if (doc != null) enqueue(doc);
            }
        } catch (IOException e) {
            LOG.error(e); //something wrong with the course page
        }
    }

    private DownloadableDocument createDocument(String downloadPageURL, HtmlPage coursePage) {
        String courseName = gatewayCrawler.fetchCourseName(coursePage);
        try {
            Page downloadPage = browser.getPage(downloadPageURL);
            return documentCrawler.fetchDocument(downloadPage, courseName);
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
    }

    private void enqueue(DownloadableDocument doc) {
        try {
            queue.put(doc);
            produced++;
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }
}


