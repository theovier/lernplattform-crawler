import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            CourseCrawler courseCrawler = new CourseCrawler("Sommersemester", "2016");
            PDFGatewayCrawler gatewayCrawler = new PDFGatewayCrawler();
            PDFCrawler pdfCrawler = new PDFCrawler();


            List<String> courseLinks = courseCrawler.fetchCourseLinks(overviewPage);

            Map<HtmlPage, List<String>> pdfLinks = new HashMap<>();

            courseLinks.stream().forEach((link) -> {
                try {
                    final HtmlPage page = browser.getPage(link);
                    pdfLinks.put(page, gatewayCrawler.fetchPDFGateLinks(page));
                } catch (IOException e) {
                    System.out.println("error beim downloaden");
                }
            });

            List<PDFDocument> pdfDocuments = new ArrayList<>();

            pdfLinks.entrySet().stream().forEach((set) -> {
                String courseName = gatewayCrawler.fetchCourseName(set.getKey());

                set.getValue().forEach(gatewayLink -> {
                    try {
                        Page pagee = browser.getPage(gatewayLink);

                        if (pagee instanceof HtmlPage) {
                            HtmlPage pageee = (HtmlPage) pagee;

                            pdfDocuments.add(pdfCrawler.getPDFDocument(pageee, courseName));
                        } else {
                            System.err.println(pagee.getUrl());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });

            pdfDocuments.parallelStream().forEach(pdfDocument -> {
                try {
                    Downloader.downloadPDF(pdfDocument, browser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Downloader.showCreatedFolder();


            /*try {
                for (String courseLink : ) {
                    HtmlPage page = browser.getPage(courseLink);
                    for (String gatewayLink : gatewayCrawler.fetchPDFGateLinks(page)) {
                        String courseName = gatewayCrawler.fetchCourseName(page);
                        page = browser.getPage(gatewayLink);
                        PDFDocument pdf = pdfCrawler.getPDFDocument(page, courseName);
                        Downloader.downloadPDF(pdf, browser);
                    }
                }

            } catch (IOException e) {
                System.out.println("error beim downloaden");
            }
*/
        }

   //     System.exit(0);
    }




    //todo show changelog/liste wenn fertig mit download?
    //todo select semester based dropdown (based on current year +-2)
}

