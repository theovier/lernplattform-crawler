package com.lailaps.download;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.lailaps.*;
import javafx.application.Platform;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Downloader implements ObservableDownloadSource {

    private static final Logger LOG = Logger.getLogger(Downloader.class);
    private static final int BUFFER_SIZE = 8192;
    private String downloadDirectory;
    private Browser browser = new Browser();
    private DownloadStatistics statistics = new DownloadStatistics();
    private List<DownloadObserver> observers = new ArrayList<>();
    private StopWatch stopWatch = new StopWatch();

    public Downloader (CookieManager cookieManager) {
        this.browser.setCookieManager(cookieManager);
    }

    void start() {
        stopWatch.start();
    }

    public void download(DownloadableDocument doc) {
        Path target = getFilePath(doc);
        boolean alreadyExists = Files.exists(target);
        if (!alreadyExists) {
            download(doc, target);
        } else {
            statistics.incrementSkippedCount();
            notifyObserversSkipped(doc);
        }
    }

    private Path getFilePath(DownloadableDocument doc) {
        String location = downloadDirectory + doc.getSaveLocation();
        return Paths.get(location);
    }

    private void download(DownloadableDocument doc, Path target) {
        try {
            makeDirectories(target);
            saveDocument(doc, target);
            statistics.incrementDownloadCount();
        } catch (IOException e) {
            LOG.error(e);
            statistics.incrementFailedCount();
            notifyObserversFailed(doc, e);
        }
    }

    private static void makeDirectories(Path target) throws IOException {
        File containingDirectory = target.getParent().toFile();
        containingDirectory.mkdirs();
    }

    private void saveDocument(DownloadableDocument document, Path target) throws IOException {
        Page downloadPage = browser.getPage(document.getDownloadLink());
        WebResponse response = downloadPage.getWebResponse();
        document.setSize(response.getContentLength());
        try (CountingInputStream in = new CountingInputStream(response.getContentAsStream())) {
            copy(in, target, document);
        }
    }

    private void copy(CountingInputStream in, Path target, DownloadableDocument document) throws IOException {
        try (OutputStream sink = Files.newOutputStream(target)) {
            notifyObserversStart(document);
            copyWithNotifyProgress(in, sink, document);
        }
    }

    private void copyWithNotifyProgress(CountingInputStream source, OutputStream sink, DownloadableDocument document) throws IOException {
        int n;
        byte[] buffer = new byte[BUFFER_SIZE];
        while((n = source.read(buffer)) > 0) {
            sink.write(buffer, 0, n);
            double progress = (double) source.getByteCount() / document.getSize();
            notifyObserversProgress(document, progress);
        }
    }

    void onTermDiscovered(String term) {
        downloadDirectory = PreferencesManager.getDirectory() + File.separator + term + File.separator;
        statistics.setDownloadFolderLocation(downloadDirectory);
    }

    void finishDownloading() {
        stopWatch.stop();
        long downloadTime = stopWatch.getTime();
        statistics.setElapsedTime(downloadTime);
        notifyObserversEnd(statistics);
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
