package com.lailaps;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.lailaps.crawler.TermCrawler;
import com.lailaps.download.*;
import com.lailaps.login.LoginClient;
import com.lailaps.login.LoginCredentials;
import com.lailaps.ui.*;
import javafx.application.Platform;
import com.gargoylesoftware.htmlunit.CookieManager;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Director {

    private LoginClient loginClient = new LoginClient();
    private CookieManager loginCookieManager;
    private Downloader downloader;
    private DocumentProducer producer;
    private DownloadScheduler consumer;
    private Thread producerThread, consumerThread;
    private BlockingQueue<DownloadableDocument> documentQueue = new LinkedBlockingQueue<>(100);
    private LoginScreenController loginScreenController;
    private ScreenContainer screenContainer;

    public Director (LoginScreenController loginScreenController) {
        this.loginScreenController = loginScreenController;
        this.screenContainer = loginScreenController.getParentScreen();
    }

    public void start(LoginCredentials credentials) {
        //start new thread, otherwise the UI is frozen.
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
        PreferencesManager.setUsername(username);
    }

    private void startDownload() {
        prepareDownload();
        startThreads(); //todo threadPoolExecutioner
    }

    private void startThreads() {
        producerThread.start();
        consumerThread.start();
    }

    private void prepareDownload() {
        prepareCookieManager();
        initDownloader();
        initThreads();
        initDownloadScreen();
    }

    private void prepareCookieManager() {
        loginCookieManager = loginClient.getBrowserCookieManager();
    }

    private void initDownloader() {
        downloader = new Downloader(loginCookieManager);
    }

    private void initThreads() {
        HtmlPage overview = loginClient.getOverviewPage();
        producer = new DocumentProducer(documentQueue, downloader, loginCookieManager, overview);
        consumer = new DownloadScheduler(documentQueue, downloader);
        producerThread = new Thread(producer);
        consumerThread = new Thread(consumer);
    }

    private void initDownloadScreen() {
        screenContainer.showScreen(ScreenType.DOWNLOAD);
        Controllable controller = screenContainer.getScreenController(ScreenType.DOWNLOAD);
        if (controller instanceof DownloadObserver) {
            DownloadObserver observer = (DownloadObserver) controller;
            downloader.addObserver(observer);
        }
    }
}
