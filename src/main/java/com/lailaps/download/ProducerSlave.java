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

public class ProducerSlave implements Runnable {

    private static final Logger LOG = Logger.getLogger(ProducerSlave.class);

    private Browser browser = new Browser();
    private BlockingQueue<DownloadableDocument> queue;
    private String courseURL;

    public ProducerSlave(String courseURL, BlockingQueue<DownloadableDocument> queue, CookieManager cookieManager) {
        this.courseURL = courseURL;
        this.browser.setCookieManager(cookieManager);
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            HtmlPage coursePage = browser.getPage(courseURL);
            fetchDocumentsForCourse(coursePage);
        } catch (IOException e) {
            LOG.error(e); //something wrong with the course page
        } finally {
            cleanUp();
        }
    }

    private void fetchDocumentsForCourse(HtmlPage coursePage) {
        List<String> downloadGatewayURLs = GatewayCrawler.fetchDownloadLinks(coursePage);
        //todo insert here List<String> xyz = folderCrawler.fetch
        for (String downloadGatewayURL : downloadGatewayURLs) {
            DownloadableDocument doc = fetchDocument(downloadGatewayURL, coursePage);
            if (doc != null) {
                enqueue(doc);
            } else {
                LOG.warn("is null: " + downloadGatewayURL);
            }
        }
    }

    private DownloadableDocument fetchDocument(String downloadPageURL, HtmlPage coursePage) {
        String courseName = GatewayCrawler.fetchCourseName(coursePage);
        try {
            Page downloadPage = browser.getPage(downloadPageURL);
            return DocumentCrawler.fetchDocument(downloadPage, courseName);
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
