package com.lailaps.crawler;

import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

public class GatewayCrawler {

    private static final String RESOURCEPATH = "https://campusapp01.hshl.de/mod/resource/view.php?id=";

    public static List<String> fetchDownloadLinks(HtmlPage coursePage) {
        List<String> downloadLinks = new ArrayList<String>();
        List<String> resourceIDs = fetchResourceIDs(coursePage);
        resourceIDs.forEach(id -> downloadLinks.add(RESOURCEPATH + id));
        return downloadLinks;
    }

    private static List<String> fetchResourceIDs(HtmlPage coursePage) {
        List<String> courseIDs = new ArrayList<>();
        List<?> courseListItems = coursePage.getByXPath("//li[contains(@class, 'activity resource modtype_resource')]");
        getIDs(courseListItems).forEach(id -> courseIDs.add(id));
        return courseIDs;
    }

    private static List<String> getIDs(List<?> uncastedListItems) {
        List <String> ids = new ArrayList<>();
        uncastedListItems.forEach(item -> {
            HtmlListItem listItem = (HtmlListItem) item;
            String numericID = getNumericID(listItem.getId());
            ids.add(numericID);
        });
        return ids;
    }

    private static String getNumericID(String ID) {
        return ID.split("-")[1];
    }

    public static String fetchCourseName(HtmlPage coursePage) {
        HtmlHeading1 filename = coursePage.getFirstByXPath("//div[@class='page-header-headings']//h1");
        return clearCourseName(filename.asText());
    }

    private static String clearCourseName(String name) {
        String clearedName =  name.replace('/', '&');
        clearedName = clearedName.replace(':', ';');
        return clearedName;
    }
}
