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

    //todo refactor! rename!
    public static boolean downloadPDF(PDFDocument doc, WebClient browser) throws IOException {
        directory = PreferencesManager.getInstance().getDirectory() + getRoot();
        String courseDirectory = directory + doc.getFolderName() + "/";
        String documentDirectory = courseDirectory + doc.getName() + doc.getFileExtension();
        Path target = Paths.get(documentDirectory);
        if (Files.exists(target)) return false;
        createDirectory(courseDirectory);
        Page downloadPage = browser.getPage(doc.getDownloadLink());
        downloadFile(downloadPage, target);
        System.out.println(doc); //todo add to log
        return true;
    }

    public static void createDirectory(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private static String getRoot() {
        return "/" + rootName + "/";
    }

    //rename
    private static void downloadFile(Page downloadPage, Path target) throws IOException {
        try (InputStream source = downloadPage.getWebResponse().getContentAsStream()) {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
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
