package com.lailaps.crawler;

import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

public class TermCrawler extends Crawler {

    private static final String LABEL_XPATH = "//label[@title]";
    private static final String SUMMER_TERM = "Sommersemester";
    private static final String WINTER_TERM = "Wintersemester";

    public Term fetchCurrentTerm(HtmlPage overviewPage) {
        List<Term> terms = fetchTerms(overviewPage);
        if (terms.isEmpty()) throw new IllegalStateException();
        return terms.get(0);
    }

    public List<Term> fetchTerms(HtmlPage overviewPage) {
        List<Term> terms = new ArrayList<>();
        List<HtmlLabel> termLabels = getTermLabels(overviewPage);
        for (HtmlLabel label : termLabels) {
            Term term = new Term(label);
            terms.add(term);
        }
        return terms;
    }

    private List<HtmlLabel> getTermLabels(HtmlPage overviewPage) {
        List<HtmlLabel> termLabels = new ArrayList<>();
        List<HtmlLabel> labels = fetchLabelsWithAnyTitle(overviewPage);
        for (HtmlLabel label : labels)
            if (isTermLabel(label))
                termLabels.add(label);
        return termLabels;
    }

    private List<HtmlLabel> fetchLabelsWithAnyTitle(HtmlPage page) {
        return (List<HtmlLabel>) page.getByXPath(LABEL_XPATH);
    }

    private boolean isTermLabel(HtmlLabel label) {
        String text = label.toString();
        return text.contains(WINTER_TERM) || text.contains(SUMMER_TERM);
    }

}
