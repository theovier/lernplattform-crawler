import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Downloader {

    //todo make selectable, get filename
    private static final String DESKTOP_PATH = System.getProperty("user.home") + "/Desktop/lailaps/";

    public static boolean downloadPDF(Page sourcePage) {
        try {
            InputStream in = sourcePage.getWebResponse().getContentAsStream();
            Files.copy(in, Paths.get(DESKTOP_PATH + "/someFile.pdf"), StandardCopyOption.REPLACE_EXISTING);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void downloadPDF(PDFDocument doc, WebClient browser) throws IOException {
        Page page = browser.getPage(doc.getDownloadLink());
        InputStream in = page.getWebResponse().getContentAsStream();
        Files.copy(in, Paths.get(DESKTOP_PATH + doc.getName() + ".pdf"), StandardCopyOption.REPLACE_EXISTING);
        in.close();
        System.out.println(doc);
    }
}
