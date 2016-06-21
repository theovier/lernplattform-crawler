import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args ) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            //todo differentiate
        }
        Window window = new Window();
    }

    //todo new manager class?
    public static void startLogin(LoginCredentials credentials) {
        LoginClient client = new LoginClient(credentials);
        boolean success = false;
        HtmlPage overviewPage = null;
        WebClient browser = null;
        try {
            overviewPage = client.establishConnection();
            browser = client.getWebClient();
            success = true;
        } catch (WrongCredentialsException e) {
            System.out.println("falsche credentials");
        } catch (IOException e) {
            System.out.println("internet probleme?");
        }

        if (success) {
            Downloader.rootName = "Sommersemester 2016";
            CourseCrawler courseCrawler = new CourseCrawler("Sommersemester", "2016");
            PDFGatewayCrawler gatewayCrawler = new PDFGatewayCrawler();
            PDFCrawler pdfCrawler = new PDFCrawler();

            try {
                for (String courseLink : courseCrawler.fetchCourseLinks(overviewPage)) {
                    HtmlPage page = browser.getPage(courseLink);
                    for (String gatewayLink : gatewayCrawler.fetchPDFGateLinks(page)) {
                        String courseName = gatewayCrawler.fetchCourseName(page);
                        page = browser.getPage(gatewayLink);
                        PDFDocument pdf = pdfCrawler.getPDFDocument(page, courseName);
                        Downloader.downloadPDF(pdf, browser);
                    }
                }
                Downloader.showCreatedFolder();
            } catch (IOException e) {
                System.out.println("error beim downloaden");
            }

        }

   //     System.exit(0);
    }




    //todo show changelog/liste wenn fertig mit download?
    //todo select semester based dropdown (based on current year +-2)
}

