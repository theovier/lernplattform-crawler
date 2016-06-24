import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args ) {
        Window window = new Window();
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


            LinkedBlockingQueue<DownloadableDocument> downloadableDocuments = new LinkedBlockingQueue<>(100);

            WebClient downloadBrowser = new WebClient();
            downloadBrowser.setCookieManager(browser.getCookieManager());

            Downloader downloader = new Downloader(downloadBrowser);
            downloader.setRootDirName("Sommersemester 2016");

            DocumentProducer producer = new DocumentProducer(downloadableDocuments, browser, overviewPage);
            DownloadScheduler consumer = new DownloadScheduler(downloadableDocuments, downloader, producer);
            Thread producerThread = new Thread(producer);
            Thread consumerThread = new Thread(consumer);
            producerThread.start();
            consumerThread.start();
        }
    }


    //todo show changelog/liste wenn fertig mit download?
}

