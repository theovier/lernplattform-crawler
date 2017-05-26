package com.lailaps.login;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.html.*;
import com.lailaps.Browser;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;

public class LoginClient {

    private Browser browser = new Browser();
    private HtmlPage currentPage;
    private HtmlSubmitInput loginButton;
    private HtmlTextInput loginUser;
    private HtmlPasswordInput loginPassword;

    public LoginClient() {
        disableHttpLogging();
        initBrowser();
    }

    private void initBrowser() {
        browser.getOptions().setJavaScriptEnabled(true);
        browser.getOptions().setRedirectEnabled(true);
        browser.getOptions().setThrowExceptionOnScriptError(false);
        browser.getCookieManager().setCookiesEnabled(true);
        browser.getCache().setMaxSize(0);
    }

    private void disableHttpLogging() {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
        Logger.getLogger("org.apache.http").setLevel(Level.OFF);
        Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
    }

    public boolean login(LoginCredentials credentials) throws IOException {
        loginToCampusPortal(credentials);
        loginToLernplattform();
        return true;
    }

    private void loginToCampusPortal(LoginCredentials credentials) throws IOException {
        currentPage = browser.getPage("https://campusportal.hshl.de");
        getLoginWidgets();
        setLoginValues(credentials);
        currentPage = loginButton.click();
        checkForLoginError();
    }

    private void getLoginWidgets() {
        loginButton = (HtmlSubmitInput) currentPage.getElementById("submit_button");
        loginUser = (HtmlTextInput) currentPage.getElementById("user_name");
        loginPassword = (HtmlPasswordInput) currentPage.getElementById("password");
    }

    private void setLoginValues(LoginCredentials credentials) {
        if (loginUser == null || loginPassword == null) {
            throw new IllegalStateException("site is not loaded correctly.");
        }
        loginUser.setValueAttribute(credentials.getUser());
        loginPassword.setValueAttribute(credentials.getPassword());
    }

    private void checkForLoginError() throws WrongCredentialsException {
        if (currentPage.asText().contains("Authentication failed") ||
                currentPage.asText().contains("Auto-login failed"))
            throw new WrongCredentialsException();
    }

    private void loginToLernplattform() throws IOException {
        currentPage = browser.getPage("https://campusapp01.hshl.de/auth/ldap/ntlmsso_attempt.php");
        grabLoginCookies();
        currentPage = browser.getPage("https://campusapp01.hshl.de/auth/ldap/ntlmsso_finish.php");
    }

    private void grabLoginCookies() throws IOException {
        browser.getPage(getPHPCookieURL());
    }

    private String getPHPCookieURL() throws WrongCredentialsException {
        String cookieURL;
        try {
            String source = currentPage.asXml();
            int begin = source.indexOf("https://campusapp01.hshl.de/auth/ldap/ntlmsso_magic.php?sesskey=");
            int end = source.indexOf( '\"', begin);
            cookieURL = source.substring(begin, end);
        } catch (StringIndexOutOfBoundsException e) {
            //if the string can't be found, you are not logged in.
            throw new WrongCredentialsException();
        }
        return cookieURL;
    }

    public CookieManager getBrowserCookieManager() {
        return browser.getCookieManager();
    }

    public HtmlPage getOverviewPage() {
        return browser.getCurrentPage();
    }
}
