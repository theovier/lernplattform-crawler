package com.lailaps.download;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.lailaps.PreferencesManager;
import javafx.application.Platform;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class DownloadScheduler implements Runnable, DownloadObserver, ObservableDownloadSource {

    private final static int SLAVE_POOL_SIZE = 5;
    private final static int SLAVE_TIMEOUT = 10;
    private static final Logger LOG = Logger.getLogger(DownloadScheduler.class);

    private ExecutorService executor = Executors.newFixedThreadPool(SLAVE_POOL_SIZE);
    private List<DownloadSlave> slaves = new ArrayList<>(SLAVE_POOL_SIZE);
    private List<DownloadObserver> observers = new ArrayList<>();
    private BlockingQueue<DownloadableDocument> queue;
    private CookieManager cookieManager;
    private boolean isRunning = true;
    private String term, downloadDirectoryLocation;
    private StopWatch stopWatch = new StopWatch();
    private DownloadStatistics statistics = new DownloadStatistics();

    public DownloadScheduler(BlockingQueue queue, CookieManager cookieManager, String directoryFriendlyTerm) {
        this.queue = queue;
        this.cookieManager = cookieManager;
        this.term = directoryFriendlyTerm;
        this.statistics.setDownloadFolderLocation(downloadDirectoryLocation);
    }

    @Override
    public void run() {
        initialize();
        startDownloadSlaves();
        while (isRunning) {
            isRunning = isQueueStillRelevant();
        }
        cleanUp();
        stopAllDownloadSlaves();
        notifyObserversEnd(statistics);
    }

    private void initialize() {
        stopWatch.start();
        downloadDirectoryLocation = PreferencesManager.getDirectory() + File.separator + term + File.separator;
    }

    private void startDownloadSlaves() {
        for (int i = 0; i < SLAVE_POOL_SIZE; i++) {
            DownloadSlave slave = new DownloadSlave(queue, cookieManager, downloadDirectoryLocation);
            slave.addObserver(this);
            slaves.add(slave);
            executor.execute(slave);
        }
    }

    private boolean isQueueStillRelevant() {
        return !(queue.peek() instanceof PoisonToken);
    }

    private void cleanUp() {
        stopWatch.stop();
        statistics.setElapsedTime(stopWatch.getTime());
    }

    private void stopAllDownloadSlaves() {
        for (DownloadSlave slave : slaves) {
            slave.stop();
        }
        try {
            executor.shutdown();
            executor.awaitTermination(SLAVE_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }

    @Override
    public void onDownloadStarted(DownloadableDocument document) {
        notifyObserversStart(document);
    }

    @Override
    public void onDownloadProgress(DownloadableDocument document, double currentProgress) {
        notifyObserversProgress(document, currentProgress);
    }

    @Override
    public void onDownloadSkipped(DownloadableDocument skippedDocument) {
        statistics.incrementSkippedCount();
        notifyObserversSkipped(skippedDocument);
    }

    @Override
    public void onDownloadFailed(DownloadableDocument failedDocument, Exception cause) {
        statistics.incrementFailedCount();
        notifyObserversFailed(failedDocument, cause);
    }

    @Override
    public void onFinishedDownloading(DownloadStatistics nullDummy) {
        //here used for finishing one download, not the whole process.
        statistics.incrementDownloadCount();
    }

    @Override
    public void addObserver(DownloadObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObserversStart(DownloadableDocument downloadedDocument) {
        Platform.runLater(() ->
                observers.forEach(observer -> observer.onDownloadStarted(downloadedDocument))
        );
    }

    @Override
    public void notifyObserversProgress(DownloadableDocument downloadableDocument, double progress) {
        Platform.runLater(() ->
                observers.forEach(observer -> observer.onDownloadProgress(downloadableDocument, progress))
        );
    }

    @Override
    public void notifyObserversSkipped(DownloadableDocument skippedDocument) {
        Platform.runLater(() ->
                observers.forEach(observer -> observer.onDownloadSkipped(skippedDocument))
        );
    }

    @Override
    public void notifyObserversFailed(DownloadableDocument failedDocument, Exception cause) {
        Platform.runLater(() ->
                observers.forEach(observer -> observer.onDownloadFailed(failedDocument, cause))
        );
    }

    @Override
    public void notifyObserversEnd(DownloadStatistics statistics) {
        Platform.runLater(() ->
                observers.forEach(observer -> observer.onFinishedDownloading(statistics))
        );
    }
}
