package com.lailaps.crawler;

import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.lailaps.download.DownloadableDocument;

public class DocumentCrawler extends Crawler {

    public static final String DOWNLOAD_START = "window.open('";
    public static final String DOWNLOAD_END = "\',";
    public static final String FILENAME_XPATH = "//div[@role='main']//h2";

    private static final String DEFAULT_EXTENSION = ".pdf";

    private String fetchFileName(HtmlPage currentPage) {
        HtmlHeading2 filename = currentPage.getFirstByXPath(FILENAME_XPATH);
        return clearName(filename.asText());
    }

    //todo regex
    private String clearName(String filename) {
        String clearedName =  filename.replace('/', '&');
        clearedName = clearedName.replace(':', ';');
        return clearedName;
    }

    private String fetchDownloadLink(String content) {
        int begin = content.indexOf(DOWNLOAD_START);
        int end = content.indexOf(DOWNLOAD_END, begin);
        return content.substring(begin + DOWNLOAD_START.length(), end);
    }

    private String fetchFileExtension(String link) {
        int lastDotIndex = link.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return link.substring(lastDotIndex);
        } else {
            return DEFAULT_EXTENSION;
        }
    }

    public DownloadableDocument getDocument(HtmlPage gatewayPage, String courseName) {
        String pageContent = gatewayPage.asXml();
        String name = fetchFileName(gatewayPage);
        String downloadLink = fetchDownloadLink(pageContent);
        String extension = fetchFileExtension(downloadLink);
        return new DownloadableDocument(name, downloadLink, courseName, extension);
    }
}
