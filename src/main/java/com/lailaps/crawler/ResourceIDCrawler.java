package com.lailaps.crawler;


import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

public class ResourceIDCrawler {

    protected static List<String> fetchResourceIDs(HtmlPage coursePage, final String LIST_ITEMS_XPATH) {
        List<String> courseIDs = new ArrayList<>();
        List<?> courseListItems = coursePage.getByXPath(LIST_ITEMS_XPATH);
        getIDs(courseListItems).forEach(id -> courseIDs.add(id));
        return courseIDs;
    }

    protected static List<String> getIDs(List<?> uncastedListItems) {
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
}
