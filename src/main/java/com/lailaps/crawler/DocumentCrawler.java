package com.lailaps.crawler;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.lailaps.download.DownloadableDocument;

public class DocumentCrawler {

    private static final String DOWNLOAD_START = "window.open('";
    private static final String DOWNLOAD_END = "\',";
    private static final String FILENAME_XPATH = "//div[@role='main']//h2";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String DEFAULT_EXTENSION = ".pdf";
    private static final String ZIP_EXTENSION = ".zip";

    public static DownloadableDocument fetchDocument(Page possibleGatewayPage, String courseName, Term term) {
        if (possibleGatewayPage.isHtmlPage()) {
            HtmlPage gatewayPage = (HtmlPage) possibleGatewayPage;
            return getDocumentFromGateway(gatewayPage, courseName, term);
        } else {
            return getDocumentWithoutGateway(possibleGatewayPage, courseName, term);
        }
    }

    public static DownloadableDocument getDocumentFromGateway(HtmlPage gatewayPage, String courseName, Term term) {
        String pageContent = gatewayPage.asXml();
        String name = fetchFileName(gatewayPage);
        String downloadLink = fetchDownloadLink(pageContent);
        String extension = fetchFileExtension(downloadLink);
        return new DownloadableDocument(name, downloadLink, courseName, extension, term);
    }

    //some files can't be opened in a new tab ('gateway'). the download starts directly by calling the URL.
    private static DownloadableDocument getDocumentWithoutGateway(Page directDownloadPage, String courseName, Term term) {
        String downloadPageURL = directDownloadPage.getUrl().toString();
        WebResponse response = directDownloadPage.getWebResponse();
        String contentDispositionHeader = response.getResponseHeaderValue("Content-Disposition");
        String filename = fetchFileNameFromContentDisposition(contentDispositionHeader);
        String extension = fetchFileExtension(filename);
        String name = filename.substring(0, filename.indexOf(extension));
        long size = fetchFileSizeFromResponse(response);

        //todo find better solution for the zip-name hack
        if (ZIP_EXTENSION.equals(extension)) {
            name = clearZIPName(name);
            name += "-" + size;
        }
        return new DownloadableDocument(name, downloadPageURL, courseName, extension, size, term);
    }

    private static String fetchFileName(HtmlPage currentPage) {
        HtmlHeading2 filename = currentPage.getFirstByXPath(FILENAME_XPATH);
        return cleanFileName(filename.asText());
    }

    private static String cleanFileName(String filename) {
        String clearedName =  filename.replace('/', '&');
        return clearedName.replace(':', ';');
    }

    private static String fetchDownloadLink(String content) {
        int begin = content.indexOf(DOWNLOAD_START);
        int end = content.indexOf(DOWNLOAD_END, begin);
        return content.substring(begin + DOWNLOAD_START.length(), end);
    }

    private static String fetchFileExtension(String link) {
        int lastDotIndex = link.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return link.substring(lastDotIndex);
        } else {
            return DEFAULT_EXTENSION;
        }
    }

    private static String fetchFileNameFromContentDisposition(String contentDisposition) {
        //Content-Disposition = attachment; filename="Vorlesung 1 - Haeufigkeiten.ipynb"
        int firstDoubleQuoteIndex = contentDisposition.indexOf(DOUBLE_QUOTE);
        int lastDoubleQuoteIndex = contentDisposition.lastIndexOf(DOUBLE_QUOTE);
        return contentDisposition.substring(firstDoubleQuoteIndex + 1, lastDoubleQuoteIndex);
    }

    private static long fetchFileSizeFromResponse(WebResponse response) {
        return response.getContentLength();
    }

    private static String clearZIPName(String filename) {
        //server adds the date to every zip file. e.g. filename-20170410.zip
        if (filename.contains("-")) {
            return filename.substring(0, filename.lastIndexOf("-"));
        } else {
            return filename;
        }
    }
}
