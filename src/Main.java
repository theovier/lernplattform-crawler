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
            CourseCrawler courseCrawler = new CourseCrawler();
            PDFGatewayCrawler gatewayCrawler = new PDFGatewayCrawler();
            PDFCrawler pdfCrawler = new PDFCrawler();


            //todo linkedblockingQueue


            //LinkedBlockingQueue queue = new LinkedBlockingQueue(10); //generic

            //todo beat 1:40min
            List<PDFDocument> pdfDocuments = new ArrayList<>();

            try {
                for (String courseLink : courseCrawler.fetchCourseLinks(overviewPage)) {
                    HtmlPage page = browser.getPage(courseLink);
                    for (String gatewayLink : gatewayCrawler.fetchPDFGateLinks(page)) {
                        String courseName = gatewayCrawler.fetchCourseName(page);
                        page = browser.getPage(gatewayLink);
                        pdfDocuments.add(pdfCrawler.getPDFDocument(page, courseName));
                    }
                }
            } catch (IOException e) {
                System.out.println("error beim downloaden");
            }

            pdfDocuments.parallelStream().forEach(pdfDocument -> {
                try {
                    Downloader.downloadPDF(pdfDocument, browser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Downloader.showCreatedFolder();
        }
    }


    //todo show changelog/liste wenn fertig mit download?
}

