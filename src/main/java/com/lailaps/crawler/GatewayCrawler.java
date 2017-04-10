package com.lailaps.crawler;

import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

public class GatewayCrawler extends ResourceIDCrawler {

    private static final String COURSE_NAME_XPATH = "//div[@class='page-header-headings']//h1";
    private static final String RESOURCE_PATH = "https://campusapp01.hshl.de/mod/resource/view.php?id=";
    private static final String RESOURCE_CSS_CLASS = "activity resource modtype_resource";
    private static final String LIST_ITEMS_XPATH = String.format(
            "//li[contains(@class, '%s')]",
            RESOURCE_CSS_CLASS
    );

    public static List<String> fetchDownloadLinks(HtmlPage coursePage) {
        List<String> downloadLinks = new ArrayList<String>();
        List<String> resourceIDs = ResourceIDCrawler.fetchResourceIDs(coursePage, LIST_ITEMS_XPATH);
        resourceIDs.forEach(id -> downloadLinks.add(RESOURCE_PATH + id));
        return downloadLinks;
    }

    public static String fetchCourseName(HtmlPage coursePage) {
        HtmlHeading1 filename = coursePage.getFirstByXPath(COURSE_NAME_XPATH);
        return clearCourseName(filename.asText());
    }

    private static String clearCourseName(String name) {
        String clearedName =  name.replace('/', '&');
        clearedName = clearedName.replace(':', ';');
        return clearedName;
    }
}
