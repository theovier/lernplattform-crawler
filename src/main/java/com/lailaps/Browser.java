package com.lailaps;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Browser extends WebClient {

    public HtmlPage getCurrentPage() {
        return (HtmlPage) getCurrentWindow().getEnclosedPage();
    }

    public Browser() {
        super();
        getOptions().setJavaScriptEnabled(true);
        getOptions().setRedirectEnabled(true);
        getOptions().setThrowExceptionOnScriptError(false);
        getCookieManager().setCookiesEnabled(true);
        getCache().setMaxSize(0);
    }
}
