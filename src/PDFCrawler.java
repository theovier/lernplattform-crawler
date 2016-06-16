import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

import java.io.IOException;

public class PDFCrawler {
    private WebClient webClient;

    public PDFCrawler(WebClient webClient) {
        this.webClient = webClient;
    }

    public void startDemo() {
        String source = "https://campusapp01.hshl.de/pluginfile.php/157024/mod_resource/content/1/BackendTec_00_%C3%9Cbersicht.pdf";
        try {
            Page page = webClient.getPage(source);
            Downloader.downloadPDF(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startCrawling() {

    }

}
