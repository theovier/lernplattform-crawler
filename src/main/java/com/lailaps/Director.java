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
    private LoginWindow loginWindow;
    private ProgressWindow progressWindow;
    private LoginClient loginClient = new LoginClient();
    private Browser crawlBrowser, downloadBrowser;
    private Downloader downloader;
    private DocumentProducer producer;
    private DownloadScheduler consumer;
    private Thread producerThread, consumerThread;
    private LinkedBlockingQueue<DownloadableDocument> downloadableDocuments;

    private LoginScreenController loginScreenController;
    private ScreenContainer screenContainer;

    public Director (LoginWindow loginWindow) {
        this.loginWindow = loginWindow;
    }

    public Director (LoginScreenController loginScreenController) {
        this.loginScreenController = loginScreenController;
        this.screenContainer = loginScreenController.getParentScreen();
    }

    public void start(LoginCredentials credentials) {
        new Thread(() -> {
            Thread.currentThread().setName("com.lailaps.Director");
            //boolean loginSuccess = login(credentials);
            if (true) { //loginSuccess
                //saveUsername(credentials.getUser());
                //startDownload();
                screenContainer.showScreen(ScreenType.DOWNLOAD);
            }
        }).start();
    }

    private boolean login(LoginCredentials credentials) {
        try {
            loginClient.login(credentials);
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
        producer = new DocumentProducer(downloadableDocuments, crawlBrowser);
        consumer = new DownloadScheduler(downloadableDocuments, downloader, producer);
    }

    private void initThreads() {
        producerThread = new Thread(producer);
        consumerThread = new Thread(consumer);
    }

    private void initProgressWindow() {
        //progressWindow = new ProgressWindow();
        //progressWindow.show();
        //downloader.addObserver(progressWindow); //todo extract
    }
}
