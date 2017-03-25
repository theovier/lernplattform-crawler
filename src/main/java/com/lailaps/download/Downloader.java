package com.lailaps.download;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.lailaps.*;
import javafx.application.Platform;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Downloader implements ObservableDownloadSource {

    private static final Logger LOG = Logger.getLogger(Downloader.class);
    private static final int BUFFER_SIZE = 8192;
    private static String rootDirName;
    private static String rootDirectory;
    private Browser browser;
    private List<DownloadObserver> observers;
    private int downloadCount;

    public Downloader (Browser browser) {
        this.browser = browser;
        observers = new ArrayList<>();
    }

    public void startDownload(DownloadableDocument doc) {
        Path target = getFilePath(doc);
        boolean alreadyExists = Files.exists(target);
        if (!alreadyExists) {
            download(doc, target);
        } else {
            notifyObserversSkipped(doc);
        }
    }

    private Path getFilePath(DownloadableDocument doc) {
        String courseDirectory = rootDirectory + doc.getFolderName() + "/";
        String documentDirectory = courseDirectory + doc.getName() + doc.getFileExtension();
        return Paths.get(documentDirectory);
    }

    private void download(DownloadableDocument doc, Path target) {
        try {
            makeDirectories(target);
            saveDocument(doc, target);
            downloadCount++;
        } catch (IOException e) {
            LOG.error(e);
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

    public void showRootFolder() {
        //todo on mac not explorer.exe
        //todo change the path to contain only backslashes. + need to go one level deeper (name a directory in this dir?)
        try {
            Runtime.getRuntime().exec("explorer.exe /select," + Paths.get(rootDirectory));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeRootDir(String rootDirectoryName) {
        rootDirName = rootDirectoryName;
        rootDirectory = fetchRootDir();
    }

    private static String fetchRootDir() {
        return PreferencesManager.getInstance().getDirectory() + "/" + rootDirName + "/";
    }

    public void finishDownloading() {
        LOG.info("downloaded Documents: " + downloadCount);
        showRootFolder();
        notifyObserversEnd();
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
    public void notifyObserversEnd() {
        Platform.runLater(() ->
            observers.forEach(observer -> observer.onFinishedDownloading())
        );
    }
}
