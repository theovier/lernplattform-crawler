package com.lailaps.download;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.lailaps.Browser;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class DownloadSlave implements Runnable, ObservableDownloadSource {

    private static final int TIMEOUT = 2;
    private static final int BUFFER_SIZE = 8192;
    private static final Logger LOG = Logger.getLogger(DownloadSlave.class);

    private boolean isRunning = true;
    private BlockingQueue<DownloadableDocument> queue;
    private Browser browser = new Browser();
    private DownloadObserver observer;
    private String downloadDirectory;

    public DownloadSlave(BlockingQueue<DownloadableDocument> queue, CookieManager cookieManager, String targetDirectory) {
        this.queue = queue;
        this.browser.setCookieManager(cookieManager);
        this.downloadDirectory = targetDirectory;
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            downloadFromDocumentQueue();
        }
    }

    private void downloadFromDocumentQueue() {
        try {
            DownloadableDocument document = queue.poll(TIMEOUT, TimeUnit.SECONDS);
            handleDocument(document);
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }

    private void handleDocument(DownloadableDocument document) {
        if (document == null) return;
        if (document instanceof PoisonToken) {
            isRunning = false;
        } else {
            download(document);
        }
    }

    private void download(DownloadableDocument doc) {
        Path target = getFilePath(doc);
        boolean alreadyExists = Files.exists(target);
        if (!alreadyExists) {
            download(doc, target);
        } else {
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
            notifyObserversEnd(null);
        } catch (IOException | FailingHttpStatusCodeException e) {
            LOG.error(e);
            notifyObserversFailed(doc, e);
        }
    }

    private void makeDirectories(Path target) throws IOException {
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

    @Override
    public void addObserver(DownloadObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyObserversStart(DownloadableDocument downloadedDocument) {
        observer.onDownloadStarted(downloadedDocument);
    }

    @Override
    public void notifyObserversProgress(DownloadableDocument downloadableDocument, double progress) {
        observer.onDownloadProgress(downloadableDocument, progress);
    }

    @Override
    public void notifyObserversSkipped(DownloadableDocument skippedDocument) {
        observer.onDownloadSkipped(skippedDocument);
    }

    @Override
    public void notifyObserversFailed(DownloadableDocument failedDocument, Exception cause) {
        observer.onDownloadFailed(failedDocument, cause);
    }

    @Override
    public void notifyObserversEnd(DownloadStatistics nullDummy) {
        observer.onFinishedDownloading(null);
    }
}
