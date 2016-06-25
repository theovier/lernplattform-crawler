package com.lailaps.crawler;

import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.List;

public class TermCrawler extends Crawler{

    private static final String LABEL_XPATH = "//label[@title]";
    private static final String SUMMER_TERM = "Sommersemester";
    private static final String WINTER_TERM = "Wintersemester";
    private HtmlPage overviewPage;

    public TermCrawler(HtmlPage overviewPage) {
        this.overviewPage = overviewPage;
    }

    public String fetchCurrentTerm() {
        List<HtmlLabel> labels = fetchLabelsWithAnyTitle();
        String currentTerm = getFirstTermLabel(labels);
        return currentTerm;
    }

    private List<HtmlLabel> fetchLabelsWithAnyTitle() {
        return (List<HtmlLabel>) overviewPage.getByXPath(LABEL_XPATH);
    }

    private String getFirstTermLabel(List<HtmlLabel> labels) {
        for (HtmlLabel label : labels)
            if (isTermLabel(label))
                return label.getAttribute("title").toString();
        return null;
    }

    private boolean isTermLabel(HtmlLabel label) {
        String text = label.toString();
        return text.contains(WINTER_TERM) || text.contains(SUMMER_TERM);
    }
}
