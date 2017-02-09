package com.lailaps.download;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.lailaps.Browser;
import com.lailaps.crawler.CourseCrawler;
import com.lailaps.crawler.DocumentCrawler;
import com.lailaps.crawler.GatewayCrawler;
import com.lailaps.crawler.TermCrawler;
import com.lailaps.download.DownloadableDocument;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class DocumentProducer implements Runnable{

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

    //todo rename
    private void crawlWebsite() throws IOException {
        crawlTerm();
        List<String> courseLinks = courseCrawler.fetchCourseLinks(overviewPage);
        for (String link : courseLinks) {
            crawlCourse(link);
        }
    }

    private void crawlTerm() {
        String term = termCrawler.fetchCurrentTerm(overviewPage);
        Downloader.changeRootDir(term);
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


