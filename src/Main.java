import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args ) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            //todo differentiate
        }
        Window window = new Window();
        //window.show();
    }

    //todo new manager class?
    public static void startLogin(LoginCredentials credentials) {

        new Thread(() -> {
            Thread.currentThread().setName("Downloader");
            System.out.println("qwhjeqkwhej");
        }).start();

        LoginClient client = new LoginClient(credentials);
        boolean success = false;
        HtmlPage overviewPage = null;
        final WebClient browser = client.getWebClient();
        try {
            overviewPage = client.establishConnection();
            success = true;
        } catch (WrongCredentialsException e) {
            System.out.println("falsche credentials");
        } catch (IOException e) {
            System.out.println("internet probleme?");
        }

        if (success) {
            Downloader.rootName = "Sommersemester 2016";

            LinkedBlockingQueue<PDFDocument> downloadableDocuments = new LinkedBlockingQueue<>(100);

            WebClient downloadBrowser = new WebClient();
            downloadBrowser.setCookieManager(browser.getCookieManager());
            
            Producer producer = new Producer(downloadableDocuments, browser, overviewPage);
            Consumer consumer = new Consumer(downloadableDocuments, downloadBrowser, producer);
            Thread producerThread = new Thread(producer);
            Thread consumerThread = new Thread(consumer);
            producerThread.start();
            consumerThread.start();
        }
    }


    //todo show changelog/liste wenn fertig mit download?
}

