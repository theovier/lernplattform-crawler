package com.lailaps.download;

import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;

public class DownloadScheduler implements Runnable {

    private static final Logger LOG = Logger.getLogger(DownloadScheduler.class);
    private BlockingQueue<DownloadableDocument> queue;
    private Downloader downloader;
    private boolean shouldSchedule = true;

    public DownloadScheduler(BlockingQueue queue, Downloader downloader) {
        this.queue = queue;
        this.downloader = downloader;
    }

    @Override
    public void run() {
        while (shouldSchedule) {
            downloadFromDocumentQueue();
        }
    }

    private void downloadFromDocumentQueue() {
        try {
            DownloadableDocument document = queue.take();
            handDocumentToDownloader(document);
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }

    private void handDocumentToDownloader(DownloadableDocument document) {
        if (document instanceof PoisonToken) {
            shouldSchedule = false;
            downloader.finishDownloading();
        } else {
            downloader.startDownload(document);
        }
    }
}
