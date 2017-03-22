package com.lailaps;

import com.lailaps.download.DocumentProducer;
import com.lailaps.download.DownloadScheduler;
import com.lailaps.download.DownloadableDocument;
import com.lailaps.download.Downloader;
import com.lailaps.login.LoginClient;
import com.lailaps.login.LoginCredentials;
import com.lailaps.ui.*;
import javafx.application.Platform;
import org.apache.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;

//todo find a better name
public class Director {

    private static final Logger LOG = Logger.getLogger(Director.class);
    private LoginClient loginClient = new LoginClient();
    private Browser crawlBrowser, downloadBrowser;
    private Downloader downloader;
    private DocumentProducer producer;
    private DownloadScheduler consumer;
    private Thread producerThread, consumerThread;
    private LinkedBlockingQueue<DownloadableDocument> downloadableDocuments;
    private LoginScreenController loginScreenController;
    private ScreenContainer screenContainer;

    public Director (LoginScreenController loginScreenController) {
        this.loginScreenController = loginScreenController;
        this.screenContainer = loginScreenController.getParentScreen();
    }

    public void start(LoginCredentials credentials) {
        new Thread(() -> {
            Thread.currentThread().setName("com.lailaps.Director");
            boolean loginSuccess = login(credentials);
            if (loginSuccess) {
                saveUsername(credentials.getUser());
                startDownload();
            }
        }).start();
    }

    private boolean login(LoginCredentials credentials) {
        try {
            return loginClient.login(credentials);
        } catch (Exception e) {
            Platform.runLater(() -> loginScreenController.showLoginError(e));
        }
        return false;
    }

    private void saveUsername(String username) {
        PreferencesManager.getInstance().setUsername(username);
    }

    private void startDownload() {
        prepareDownload();
        startThreads();
    }

    private void startThreads() {
        producerThread.start();
        consumerThread.start();
    }

    private void prepareDownload() {
        initBrowsers();
        initDownloader();
        initQueue();
        initThreads();
        initProgressWindow();
    }

    private void initBrowsers() {
        initCrawlBrowser();
        initDownloadBrowser();
    }

    private void initCrawlBrowser() {
        crawlBrowser = loginClient.getBrowser();
    }

    private void initDownloadBrowser() {
        downloadBrowser = new Browser();
        downloadBrowser.setCookieManager(crawlBrowser.getCookieManager());
    }

    private void initDownloader() {
        downloader = new Downloader(downloadBrowser);
    }

    private void initQueue() {
        downloadableDocuments = new LinkedBlockingQueue<>(100);
        producer = new DocumentProducer(downloadableDocuments, downloader, crawlBrowser);
        consumer = new DownloadScheduler(downloadableDocuments, downloader, producer);
    }

    private void initThreads() {
        producerThread = new Thread(producer);
        consumerThread = new Thread(consumer);
    }

    private void initProgressWindow() {
        screenContainer.showScreen(ScreenType.DOWNLOAD);
        downloader.addObserver(screenContainer.getCurrentScreenController());
    }
}
