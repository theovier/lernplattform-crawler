package com.lailaps.download;


import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.lailaps.Browser;
import com.lailaps.crawler.DocumentCrawler;
import com.lailaps.crawler.GatewayCrawler;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ProducerThread implements Runnable {

    private static final Logger LOG = Logger.getLogger(ProducerThread.class);

    private Browser browser = new Browser();
    private GatewayCrawler gatewayCrawler = new GatewayCrawler();
    private DocumentCrawler documentCrawler = new DocumentCrawler();
    private BlockingQueue<DownloadableDocument> queue;
    private String courseURL;

    public ProducerThread(String courseURL, BlockingQueue<DownloadableDocument> queue, CookieManager cookieManager) {
        this.courseURL = courseURL;
        this.browser.setCookieManager(cookieManager);
        this.queue = queue;
    }

    @Override
    public void run() {
        fetchDocumentsForCourse(courseURL);
        cleanUp();
    }

    private void fetchDocumentsForCourse(String courseURL) {
        try {
            HtmlPage coursePage = browser.getPage(courseURL);
            List<String> downloadGatewayURLs = gatewayCrawler.fetchDownloadLinks(coursePage);
            for (String downloadGatewayURL : downloadGatewayURLs) {
                DownloadableDocument doc = fetchDocument(downloadGatewayURL, coursePage);
                if (doc != null) {
                    enqueue(doc);
                } else {
                    LOG.warn("is null: " + downloadGatewayURL);
                }
            }
        } catch (IOException e) {
            LOG.error(e); //something wrong with the course page
        }
    }

    private DownloadableDocument fetchDocument(String downloadPageURL, HtmlPage coursePage) {
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
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }

    private void cleanUp() {
        browser.close();
    }
}
