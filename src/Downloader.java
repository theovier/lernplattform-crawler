import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Downloader {

    public static String rootName = "Sommersemester 2016";
    private static String directory;

    public static void downloadPDF(PDFDocument doc, WebClient browser) throws IOException {
        directory = PreferencesManager.getInstance().getDirectory() + "/" + rootName + "/";
        String path = directory + doc.getFolderName() + "/";
        createDirectory(path);
        Page page = browser.getPage(doc.getDownloadLink());
        try (InputStream in = page.getWebResponse().getContentAsStream()) {
            Files.copy(in, Paths.get(path + doc.getName() +  doc.getFileExtension()), StandardCopyOption.REPLACE_EXISTING);
        }
        System.out.println(doc);
    }

    public static void createDirectory(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    public static void showCreatedFolder() {
        try {
            Runtime.getRuntime().exec("explorer.exe /select," + Paths.get(directory));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
