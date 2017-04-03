package com.lailaps.download;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
    private HtmlPage overviewPage;
    private CookieManager cookieManager;
    private CourseCrawler courseCrawler = new CourseCrawler();
    private String term;

    public DocumentProducer(BlockingQueue queue, CookieManager cookieManager, HtmlPage overviewPage, String term) {
        this.queue = queue;
        this.cookieManager = cookieManager;
        this.overviewPage = overviewPage;
        this.term = term;
    }

    @Override
    public void run() {
        produceDocuments();
    }

    private void produceDocuments() {
        List<CompletableFuture<Void>> producers = new ArrayList<>();
        List<String> courseURLs = courseCrawler.fetchCourseLinks(overviewPage, term);
        for (String courseURL : courseURLs) {
            ProducerThread demo = new ProducerThread(courseURL, queue, cookieManager);
            CompletableFuture<Void> producer = CompletableFuture.runAsync(demo);
            producers.add(producer);
        }
        CompletableFuture<Void>[] producerArray =  producers.toArray(new CompletableFuture[producers.size()]);
        CompletableFuture<Void> allProducersFinished = CompletableFuture.allOf(producerArray);
        allProducersFinished.thenRun(this::signalProducerStop);
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


