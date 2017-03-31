package com.lailaps.download;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.lailaps.Browser;
import com.lailaps.crawler.CourseCrawler;
import com.lailaps.crawler.TermCrawler;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public class DocumentProducer implements Runnable {

    private static final Logger LOG = Logger.getLogger(DocumentProducer.class);
    private BlockingQueue<DownloadableDocument> queue;
    private Downloader downloader;
    private HtmlPage overviewPage;
    private Browser browser = new Browser();
    private TermCrawler termCrawler = new TermCrawler();
    private CourseCrawler courseCrawler = new CourseCrawler();

    public DocumentProducer(BlockingQueue queue, Downloader downloader, CookieManager cookieManager, HtmlPage overviewPage) {
        this.queue = queue;
        this.downloader = downloader;
        this.browser.setCookieManager(cookieManager);
        this.overviewPage = overviewPage;
    }

    @Override
    public void run() {
        produceDocuments();
    }

    private void produceDocuments() {
        String currentTerm = termCrawler.fetchCurrentTerm(overviewPage);
        notifyDownloaderAboutTerm();
        fetchDocumentsForTerm(currentTerm);
    }

    private void notifyDownloaderAboutTerm() {
        String directoryFriendlyTerm = termCrawler.getDirectoryFriendlyTerm(overviewPage);
        downloader.onTermDiscovered(directoryFriendlyTerm);
    }

    private void fetchDocumentsForTerm(String currentTerm)  {
        List<CompletableFuture<Void>> producers = new ArrayList<>();
        List<String> courseURLs = courseCrawler.fetchCourseLinks(overviewPage, currentTerm);
        for (String courseURL : courseURLs) {
            ProducerThread demo = new ProducerThread(courseURL, queue, browser.getCookieManager());
            CompletableFuture<Void> producer = CompletableFuture.runAsync(demo);
            producers.add(producer);
        }
        browser.close();
        CompletableFuture<Void>[] producerArray =  producers.toArray(new CompletableFuture[producers.size()]);
        CompletableFuture<Void> allProducersFinished = CompletableFuture.allOf(producerArray);
        allProducersFinished.thenRun(() -> signalProducerStop());
    }

    private void signalProducerStop() {
        PoisonToken endSignal = new PoisonToken();
        try {
            queue.put(endSignal);
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }
}


