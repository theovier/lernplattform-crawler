import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Downloader {

    //todo make selectable, get filename
    private static final String DESKTOP_PATH = System.getProperty("user.home") + "/Desktop/lailaps/";

    //todo refactor
    public static void downloadPDF(PDFDocument doc, WebClient browser) throws IOException {
        String path = DESKTOP_PATH + doc.getFolderName() + "/";
        createDirectory(path);
        Page page = browser.getPage(doc.getDownloadLink());
        InputStream in = page.getWebResponse().getContentAsStream();
        Files.copy(in, Paths.get(path + doc.getName() + ".pdf"), StandardCopyOption.REPLACE_EXISTING);
        in.close();
        System.out.println(doc);
    }

    public static void createDirectory(String dirPath) throws IOException{
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
