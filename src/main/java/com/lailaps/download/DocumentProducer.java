package com.lailaps.download;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.lailaps.crawler.CourseCrawler;
import com.lailaps.crawler.Term;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public class DocumentProducer implements Runnable {

    private static final Logger LOG = Logger.getLogger(DocumentProducer.class);
    private BlockingQueue<DownloadableDocument> queue;
    private HtmlPage overviewPage;
    private CookieManager cookieManager;
    private Queue<Term> termQueue;

    public DocumentProducer(BlockingQueue<DownloadableDocument> queue, CookieManager cookieManager, HtmlPage overviewPage, List<Term> terms) {
        this.queue = queue;
        this.cookieManager = cookieManager;
        this.overviewPage = overviewPage;
        this.termQueue = new LinkedList<>(terms);
    }

    @Override
    public void run() {
        produce();
    }

    private void produce() {
        if (termQueue.isEmpty()) {
            signalProducerStop();
        } else {
            Term term = termQueue.poll();
            produceDocuments(term);
        }
    }

    private void produceDocuments(Term term) {
        List<CompletableFuture<Void>> producers = new ArrayList<>();
        List<String> courseURLs = CourseCrawler.fetchCourseLinks(overviewPage, term);
        for (String courseURL : courseURLs) {
            ProducerSlave slave = new ProducerSlave(courseURL, queue, term, cookieManager);
            CompletableFuture<Void> producer = CompletableFuture.runAsync(slave);
            producers.add(producer);
        }
        CompletableFuture<Void>[] producerArray = producers.toArray(new CompletableFuture[producers.size()]);
        CompletableFuture<Void> allProducersFinished = CompletableFuture.allOf(producerArray);
        allProducersFinished.thenRun(this::produce);
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


