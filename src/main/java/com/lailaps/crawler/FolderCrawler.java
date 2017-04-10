package com.lailaps.crawler;


import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

public class FolderCrawler extends ResourceIDCrawler {

    //https://campusapp01.hshl.de/mod/folder/view.php?id=75199 => https://campusapp01.hshl.de/mod/folder/download_folder.php?id=75199
    private static final String DOWNLOAD_PATH = "https://campusapp01.hshl.de/mod/folder/download_folder.php?id=";
    private static final String FOLDER_CSS_CLASS = "activity folder modtype_folder";
    private static final String LIST_ITEMS_XPATH = String.format(
            "//li[contains(@class, '%s')]",
            FOLDER_CSS_CLASS
    );

    public static List<String> fetchDownloadLinks(HtmlPage coursePage) {
        List<String> downloadLinks = new ArrayList<String>();
        List<String> resourceIDs = fetchResourceIDs(coursePage, LIST_ITEMS_XPATH);
        resourceIDs.forEach(id -> downloadLinks.add(DOWNLOAD_PATH + id));
        return downloadLinks;
    }
}
