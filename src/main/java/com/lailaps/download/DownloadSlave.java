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
    private DownloadObserver master;
    private final ResourceIDSafekeeper safekeeper;
    private final String targetDirectory;

    public DownloadSlave(BlockingQueue<DownloadableDocument> queue, CookieManager cookieManager, String targetDirectory,
                         ResourceIDSafekeeper safekeeper) {
        this.queue = queue;
        this.browser.setCookieManager(cookieManager);
        this.targetDirectory = targetDirectory;
        this.safekeeper = safekeeper;
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
        } else if (safekeeper.hasID(doc.getResourceID())) {
            notifyObserversSkipped(doc);
        } else {
            //the document has the same name as a file already in that folder, but a different Resource-ID, so rename and download it.
            DownloadableDocument renamedDocument = DownloadableDocument.getRenamedDocument(doc);
            download(renamedDocument);
        }
    }

    private Path getFilePath(DownloadableDocument doc) {
        String location = targetDirectory + File.separator + doc.getSaveLocation();
        return Paths.get(location);
    }

    private void download(DownloadableDocument doc, Path target) {
        try {
            makeDirectories(target);
            saveDocument(doc, target);
            notifyObserversSuccess(doc);
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
    public void addObserver(DownloadObserver master) {
        this.master = master;
    }

    @Override
    public void notifyObserversStart(DownloadableDocument documentToDownload) {
        master.onDownloadStarted(documentToDownload);
    }

    @Override
    public void notifyObserversProgress(DownloadableDocument downloadableDocument, double progress) {
        master.onDownloadProgress(downloadableDocument, progress);
    }

    @Override
    public void notifyObserversSkipped(DownloadableDocument skippedDocument) {
        master.onDownloadSkipped(skippedDocument);
    }

    @Override
    public void notifyObserversFailed(DownloadableDocument failedDocument, Exception cause) {
        master.onDownloadFailed(failedDocument, cause);
    }

    @Override
    public void notifyObserversSuccess(DownloadableDocument downloadedDocument) {
        master.onDownloadSuccess(downloadedDocument);
    }

    @Override
    public void notifyObserversEnd(DownloadStatistics nullDummy) {
        //empty
    }
}
