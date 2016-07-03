package com.lailaps;

import com.lailaps.download.DocumentProducer;
import com.lailaps.download.DownloadScheduler;
import com.lailaps.download.DownloadableDocument;
import com.lailaps.download.Downloader;
import com.lailaps.login.LoginClient;
import com.lailaps.login.LoginCredentials;
import com.lailaps.login.WrongCredentialsException;
import com.lailaps.ui.LoginWindow;
import com.lailaps.ui.ProgressWindow;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

//todo find a better name
public class Director {

    private LoginWindow loginWindow;
    private ProgressWindow progressWindow;
    private LoginClient loginClient;
    private Browser crawlBrowser, downloadBrowser;
    private Downloader downloader;
    private DocumentProducer producer;
    private DownloadScheduler consumer;
    private Thread producerThread, consumerThread;
    private LinkedBlockingQueue<DownloadableDocument> downloadableDocuments;

    public Director (LoginWindow loginWindow) {
        this.loginWindow = loginWindow;
        loginClient = new LoginClient();
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
        } catch (WrongCredentialsException e) {
            System.out.println("wrong login credentials"); //todo send to loginWindow
        } catch (IOException e) {
            System.out.println("connection problems"); //todo send to loginWindow
        } catch (NullPointerException e) {
            System.out.println("problems with loading the site"); //todo send to loginWindow
            e.getMessage();
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
        loginWindow.hide();
        progressWindow = new ProgressWindow();
        progressWindow.show();
        downloader.addObserver(progressWindow); //todo extract
    }
}
