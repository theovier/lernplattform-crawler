package com.lailaps;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Browser extends WebClient {

    public HtmlPage getCurrentPage() {
        return (HtmlPage) getCurrentWindow().getEnclosedPage();
    }
}
