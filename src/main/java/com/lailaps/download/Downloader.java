package com.lailaps.download;

import com.gargoylesoftware.htmlunit.Page;
import com.lailaps.*;
import javafx.application.Platform;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Downloader implements ObservableDownloadSource {

    private static String rootDirName;
    private static String rootDirectory;
    private Browser browser;
    private List<DownloadObserver> observers;

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
            Files.createDirectories(target);
            Page downloadPage = browser.getPage(doc.getDownloadLink());
            copyFileToDisc(downloadPage, target);
            notifyObserversStart(doc);
        } catch (IOException e) {
            notifyObserversFailed(doc, e);
        }
    }

    private static void copyFileToDisc(Page downloadPage, Path target) throws IOException {
        try (InputStream source = downloadPage.getWebResponse().getContentAsStream()) {
            //todo replace with own method which notifies observer? -> COUNTING INPUT STREAM?
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void showRootFolder() {
        //todo on mac not explorer.exe
        try {
            Runtime.getRuntime().exec("explorer.exe /select," + Paths.get(rootDirectory));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeRootDir(String rootDirectoryName) {
        rootDirName = rootDirectoryName;
        rootDirectory = fetchRootDir();
    }

    private static String fetchRootDir() {
        return PreferencesManager.getInstance().getDirectory() + "/" + rootDirName + "/";
    }

    public static String getRootDirName() {
        return rootDirName;
    }

    public void finishDownloading() {
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
