package com.lailaps.download;

import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;

public class DownloadScheduler implements Runnable {

    private static final Logger LOG = Logger.getLogger(DownloadScheduler.class);
    private BlockingQueue<DownloadableDocument> queue;
    private Downloader downloader;
    private boolean shouldScheduleDownloads = true;

    public DownloadScheduler(BlockingQueue queue, Downloader downloader) {
        this.queue = queue;
        this.downloader = downloader;
    }

    @Override
    public void run() {
        while (shouldScheduleDownloads) {
            downloadFromDocumentQueue();
        }
    }

    public void downloadFromDocumentQueue() {
        try {
            DownloadableDocument document = queue.take();
            if (document instanceof PoisonToken) {
                downloader.finishDownloading();
                shouldScheduleDownloads = false;
            } else {
                downloader.startDownload(document);
            }
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }
}
