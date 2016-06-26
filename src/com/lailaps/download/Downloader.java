package com.lailaps.download;

import com.gargoylesoftware.htmlunit.Page;
import com.lailaps.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Downloader implements Observable, Completable {

    private static String rootDirName;
    private static String rootDirectory;
    private Browser browser;
    private List<DownloadObserver> observers;

    public Downloader (Browser browser) {
        this.browser = browser;
        observers = new ArrayList<>();
    }

    private static String fetchRootDir() {
        return PreferencesManager.getInstance().getDirectory() + "/" + rootDirName + "/";
    }

    public void startDownload(DownloadableDocument doc) {
        Path target = getFilePath(doc);
        boolean alreadyExists = Files.exists(target);
        if (!alreadyExists) {
            download(doc, target);
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
            notifyObservers(doc);
            System.out.println(doc); //todo remove
        } catch (IOException e) {
            System.err.println("Error while downloading " + doc);
        }
    }

    private static void copyFileToDisc(Page downloadPage, Path target) throws IOException {
        try (InputStream source = downloadPage.getWebResponse().getContentAsStream()) {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void showRootFolder() {
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

    public static String getRootDirName() {
        return rootDirName;
    }

    @Override
    public void addObserver(DownloadObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(DownloadObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(DownloadableDocument downloadedDocument) {
        observers.forEach(observer -> observer.addDownload(downloadedDocument));
    }

    @Override
    public void finish() {
        showRootFolder();
        notifyObservers(null);  //todo tell observers it finished on another way
    }
}
