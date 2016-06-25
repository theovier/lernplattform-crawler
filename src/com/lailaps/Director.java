package com.lailaps;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.lailaps.login.LoginClient;
import com.lailaps.login.LoginCredentials;
import com.lailaps.login.WrongCredentialsException;
import com.lailaps.ui.Window;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

//todo find a better name
public class Director {

    private Window view;
    private HtmlPage overviewPage;
    private WebClient browser, downloadBrowser;
    private Downloader downloader;
    private DocumentProducer producer;
    private DownloadScheduler consumer;
    private Thread producerThread, consumerThread;
    private LinkedBlockingQueue<DownloadableDocument> downloadableDocuments;

    public Director (Window view) {
        this.view = view;
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
        LoginClient loginClient = new LoginClient(credentials);
        browser = loginClient.getWebClient();
        try {
            overviewPage = loginClient.establishConnection();
            return true;
        } catch (WrongCredentialsException e) {
            System.out.println("falsche credentials"); //todo send to view
        } catch (IOException e) {
            System.out.println("internet probleme?"); //todo send to view
        }
        return false;
    }

    private void startDownload() {
        prepareDownload();
        producerThread.start();
        consumerThread.start();
    }

    private void prepareDownload() {
        initDownloadBrowser();
        initDownloader();
        initQueue();
        initThreads();
    }

    private void initDownloadBrowser() {
        downloadBrowser = new WebClient();
        downloadBrowser.setCookieManager(browser.getCookieManager());
    }

    private void initDownloader() {
        downloader = new Downloader(downloadBrowser);
        downloader.setRootDirName("Sommersemester 2016"); //todo crawl this
    }

    private void initQueue() {
        downloadableDocuments = new LinkedBlockingQueue<>(100);
        producer = new DocumentProducer(downloadableDocuments, browser, overviewPage);
        consumer = new DownloadScheduler(downloadableDocuments, downloader, producer);
    }

    private void initThreads() {
        producerThread = new Thread(producer);
        consumerThread = new Thread(consumer);
    }
}
