package com.lailaps;

import com.lailaps.login.LoginClient;
import com.lailaps.login.LoginCredentials;
import com.lailaps.login.WrongCredentialsException;
import com.lailaps.ui.Window;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

//todo find a better name
public class Director {

    private Window view;
    private LoginClient loginClient;
    private Browser browser, downloadBrowser;
    private Downloader downloader;
    private DocumentProducer producer;
    private DownloadScheduler consumer;
    private Thread producerThread, consumerThread;
    private LinkedBlockingQueue<DownloadableDocument> downloadableDocuments;

    public Director (Window view) {
        this.view = view;
        loginClient = new LoginClient();
    }

    public void start(LoginCredentials credentials) {
        new Thread(() -> {
            Thread.currentThread().setName("com.lailaps.Director");

            boolean success = login(credentials);
            if (success) {
                System.out.println("Erfolgreich eingeloggt. Beginne mit Download.");
                startDownload();
            }
        }).start();
    }

    private boolean login(LoginCredentials credentials) {
        try {
            return loginClient.login(credentials);
        } catch (WrongCredentialsException e) {
            System.out.println("wrong login credentials"); //todo send to view
        } catch (IOException e) {
            System.out.println("connection problems"); //todo send to view
        }
        return false;
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
        initBrowser();
        initDownloadBrowser();
        initDownloader();
        initQueue();
        initThreads();
    }

    private void initBrowser() {
        browser = loginClient.getBrowser();
    }

    private void initDownloadBrowser() {
        downloadBrowser = new Browser();
        downloadBrowser.setCookieManager(browser.getCookieManager());
    }

    private void initDownloader() {
        downloader = new Downloader(downloadBrowser);
        downloader.setRootDirName("Sommersemester 2016"); //todo crawl this
    }

    private void initQueue() {
        downloadableDocuments = new LinkedBlockingQueue<>(100);
        producer = new DocumentProducer(downloadableDocuments, browser);
        consumer = new DownloadScheduler(downloadableDocuments, downloader, producer);
    }

    private void initThreads() {
        producerThread = new Thread(producer);
        consumerThread = new Thread(consumer);
    }
}
