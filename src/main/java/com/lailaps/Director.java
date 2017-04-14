package com.lailaps;

import com.lailaps.crawler.Term;
import com.lailaps.crawler.TermCrawler;
import com.lailaps.download.*;
import com.lailaps.login.LoginClient;
import com.lailaps.login.LoginCredentials;
import com.lailaps.ui.*;
import javafx.application.Platform;
import com.gargoylesoftware.htmlunit.CookieManager;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Director {

    private LoginClient loginClient = new LoginClient();
    private DocumentProducer producer;
    private DownloadScheduler consumer;
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
                showTermScreen();
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

    private void showTermScreen() {
        List<Term> terms = TermCrawler.fetchTerms(loginClient.getOverviewPage());
        Controllable controller = screenContainer.getScreenController(ScreenType.TERM);
        if (controller instanceof TermScreenController) {
            TermScreenController termController = (TermScreenController) controller;
            termController.setTerms(terms);
            termController.setDirector(this);
        }
        screenContainer.showScreen(ScreenType.TERM);
    }

    public void startDownloading(List<Term> termsToDownload) {
        prepareProducerConsumer(termsToDownload);
        startProducerConsumer();
        showDownloadScreen();
    }

    private void prepareProducerConsumer(List<Term> termsToDownload) {
        CookieManager loginCookieManager = getLoginCookieManager();
        prepareProducer(loginCookieManager, termsToDownload);
        prepareConsumer(loginCookieManager);
    }

    private CookieManager getLoginCookieManager() {
        return loginClient.getBrowserCookieManager();
    }

    private void prepareProducer(CookieManager cookieManager, List<Term> terms) {
        producer = new DocumentProducer(documentQueue, cookieManager, loginClient.getOverviewPage(), terms);
    }

    private void prepareConsumer(CookieManager cookieManager) {
        consumer = new DownloadScheduler(documentQueue, cookieManager);
    }

    private void startProducerConsumer() {
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
