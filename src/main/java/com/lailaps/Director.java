package com.lailaps;

import com.lailaps.crawler.Term;
import com.lailaps.crawler.TermCrawler;
import com.lailaps.download.*;
import com.lailaps.login.LoginClient;
import com.lailaps.login.LoginCredentials;
import com.lailaps.ui.*;
import javafx.application.Platform;
import com.gargoylesoftware.htmlunit.CookieManager;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Director {

    private LoginClient loginClient = new LoginClient();
    private DocumentProducer producer;
    private DownloadScheduler consumer;
    private BlockingQueue<DownloadableDocument> documentQueue = new LinkedBlockingQueue<>(100);
    private CookieManager loginCookieManager;
    private LoginScreenController loginScreenController;
    private ScreenContainer screenContainer;
    private Term currentTerm;

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
                prepareDownloading();
                startDownloading();
                showDownloadScreen();
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

    private void prepareDownloading() {
        getAndSetCookieManager();
        getAndSetCurrentTerm();
        prepareProducer();
        prepareConsumer();
    }

    private void getAndSetCookieManager() {
        loginCookieManager = loginClient.getBrowserCookieManager();
    }

    private void getAndSetCurrentTerm() {
        TermCrawler crawler = new TermCrawler();
        currentTerm = crawler.fetchCurrentTerm(loginClient.getOverviewPage());
    }

    private void prepareProducer() {
        producer = new DocumentProducer(documentQueue, loginCookieManager, loginClient.getOverviewPage(), currentTerm);
    }

    private void prepareConsumer() {
        consumer = new DownloadScheduler(documentQueue, loginCookieManager, currentTerm);
    }

    private void startDownloading() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(producer);
        executor.execute(consumer);
    }

    private void showDownloadScreen() {
        screenContainer.showScreen(ScreenType.DOWNLOAD);
        Controllable controller = screenContainer.getScreenController(ScreenType.DOWNLOAD);
        if (controller instanceof DownloadObserver) {
            DownloadObserver observer = (DownloadObserver) controller;
            consumer.addObserver(observer);
        }
    }
}
