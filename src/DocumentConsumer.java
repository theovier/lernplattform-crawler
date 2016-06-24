import java.util.concurrent.LinkedBlockingQueue;

public class DocumentConsumer implements Runnable {

    private LinkedBlockingQueue<PDFDocument> queue;
    private DocumentProducer documentProducer;
    private Downloader downloader;

    public DocumentConsumer(LinkedBlockingQueue queue, Downloader downloader, DocumentProducer documentProducer) {
        this.queue = queue;
        this.documentProducer = documentProducer;
        this.downloader = downloader;
    }

    @Override
    public void run() {
        while (documentProducer.isRunning() || !queue.isEmpty()) {
            downloadFromDocumentQueue();
        }
        downloader.showRootFolder();
    }

    public void downloadFromDocumentQueue() {
        try {
            PDFDocument pdfDocument = queue.take();
            downloader.startDownload(pdfDocument);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
