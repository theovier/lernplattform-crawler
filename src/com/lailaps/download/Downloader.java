package com.lailaps.download;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.lailaps.Browser;
import com.lailaps.PreferencesManager;
import com.lailaps.crawler.TermCrawler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Downloader {

    private static String rootDirName;
    private static String rootDirectory;
    private Browser browser;

    public Downloader (Browser browser) {
        this.browser = browser;
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
            notifyProgress(doc);
        } catch (IOException e) {
            System.err.println("Error while downloading " + doc);
        }
    }

    private static void copyFileToDisc(Page downloadPage, Path target) throws IOException {
        try (InputStream source = downloadPage.getWebResponse().getContentAsStream()) {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void notifyProgress(DownloadableDocument addedDocument) {
        System.out.println(addedDocument);
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
}
