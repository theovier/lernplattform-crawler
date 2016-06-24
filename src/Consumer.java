import com.gargoylesoftware.htmlunit.WebClient;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class Consumer implements Runnable {
    private LinkedBlockingQueue<PDFDocument> queue;
    private Producer producer;
    private WebClient browser;

    public Consumer(LinkedBlockingQueue queue, WebClient browser, Producer producer) {
        this.queue = queue;
        this.browser = browser;
        this.producer = producer;
    }

    @Override
    public void run() {

        //todo different loop condition. When producer is finished -> still working.
        while (producer.isRunning() || !queue.isEmpty()) {
            try {
                PDFDocument pdfDocument = queue.take();
                try {
                    Downloader.downloadPDF(pdfDocument, browser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Downloader.showCreatedFolder();

    }
}
