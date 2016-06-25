package com.lailaps.download;

import java.util.concurrent.LinkedBlockingQueue;

public class DownloadScheduler implements Runnable {

    private LinkedBlockingQueue<DownloadableDocument> queue;
    private DocumentProducer documentProducer;
    private Downloader downloader;

    public DownloadScheduler(LinkedBlockingQueue queue, Downloader downloader, DocumentProducer documentProducer) {
        this.queue = queue;
        this.documentProducer = documentProducer;
        this.downloader = downloader;
    }

    @Override
    public void run() {
        while (documentProducer.isRunning() || !queue.isEmpty()) {
            downloadFromDocumentQueue();
        }
        downloader.showRootFolder();
    }

    public void downloadFromDocumentQueue() {
        try {
            DownloadableDocument document = queue.take();
            downloader.startDownload(document);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
