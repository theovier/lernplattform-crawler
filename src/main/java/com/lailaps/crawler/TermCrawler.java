package com.lailaps.crawler;

import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TermCrawler {

    private static final String LABEL_XPATH = "//label[@title]";

    public static Term fetchCurrentTerm(HtmlPage overviewPage) {
        List<Term> terms = fetchTerms(overviewPage);
        if (terms.isEmpty()) throw new IllegalStateException();
        return terms.get(0);
    }

    public static List<Term> fetchTerms(HtmlPage overviewPage) {
        List<Term> terms = new ArrayList<>();
        List<HtmlLabel> termLabels = getTermLabels(overviewPage);
        for (HtmlLabel label : termLabels) {
            Term term = new Term(label);
            terms.add(term);
        }
        Collections.sort(terms, Collections.reverseOrder());
        return terms;
    }

    private static List<HtmlLabel> getTermLabels(HtmlPage overviewPage) {
        List<HtmlLabel> termLabels = new ArrayList<>();
        List<HtmlLabel> labels = fetchLabelsWithAnyTitle(overviewPage);
        for (HtmlLabel label : labels)
            if (isTermLabel(label))
                termLabels.add(label);
        return termLabels;
    }

    private static List<HtmlLabel> fetchLabelsWithAnyTitle(HtmlPage page) {
        return (List<HtmlLabel>) page.getByXPath(LABEL_XPATH);
    }

    private static boolean isTermLabel(HtmlLabel label) {
        String text = label.toString();
        return text.contains(Term.WINTER_TERM) || text.contains(Term.SUMMER_TERM);
    }

}
