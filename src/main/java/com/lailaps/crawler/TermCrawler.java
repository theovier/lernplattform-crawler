package com.lailaps.crawler;

import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.log4j.Logger;

import java.util.List;

public class TermCrawler extends Crawler{

    private static final String LABEL_XPATH = "//label[@title]";
    private static final String SUMMER_TERM = "Sommersemester";
    private static final String WINTER_TERM = "Wintersemester";

    private String currentTerm;

    public String fetchCurrentTerm(HtmlPage overviewPage) {
        List<HtmlLabel> labels = fetchLabelsWithAnyTitle(overviewPage);
        currentTerm = getFirstTermLabel(labels);
        return currentTerm;
    }

    private List<HtmlLabel> fetchLabelsWithAnyTitle(HtmlPage page) {
        return (List<HtmlLabel>) page.getByXPath(LABEL_XPATH);
    }

    private String getFirstTermLabel(List<HtmlLabel> labels) {
        for (HtmlLabel label : labels)
            if (isTermLabel(label))
                return label.getAttribute("title").toString();
        return null;
    }

    private boolean isTermLabel(HtmlLabel label) {
        String text = label.toString();
        return text.contains(WINTER_TERM) ; //|| text.contains(SUMMER_TERM); //TODO CHANGE BACK
    }

    public String getDirectoryFriendlyTerm(HtmlPage overviewPage) {
        if (currentTerm.isEmpty()) {
            fetchCurrentTerm(overviewPage);
        }
        return clearTerm(currentTerm);
    }

    private String clearTerm(String term) {
        return term.replace("/", "-");
    }







    //todo get clearedTerm, private var term, wenn nicht gefüllt nochmal neu durchrechnen.
}
