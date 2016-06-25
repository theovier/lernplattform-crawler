package com.lailaps.login;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;

public class LoginClient {

    private String username, password;
    private WebClient webClient;
    private HtmlPage currentPage;
    private HtmlSubmitInput loginButton;
    private HtmlTextInput loginUser;
    private HtmlPasswordInput loginPassword;

    public LoginClient(LoginCredentials credentials) {
        this.username = credentials.getUser();
        this.password = credentials.getPassword();
        initWebClient();
        disableLogging();
    }

    private void initWebClient() {
        webClient = new WebClient(BrowserVersion.FIREFOX_45);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getCookieManager().setCookiesEnabled(true);
    }

    private void disableLogging() {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
    }

    private void loginToCampusPortal() throws IOException {
        currentPage = webClient.getPage("https://campusportal.hshl.de");
        getLoginWidgets();
        setLoginValues();
        currentPage = loginButton.click();
        if (currentPage.asText().contains("Authentication failed")) {
            throw new WrongCredentialsException();
        }
    }

    private void getLoginWidgets() {
        loginButton = (HtmlSubmitInput) currentPage.getElementById("submit_button");
        loginUser = (HtmlTextInput) currentPage.getElementById("user_name");
        loginPassword = (HtmlPasswordInput) currentPage.getElementById("password");
    }

    private void setLoginValues() {
        loginUser.setValueAttribute(username);
        loginPassword.setValueAttribute(password);
    }

    private void loginToLernplattform() throws IOException {
        currentPage = webClient.getPage("https://campusapp01.hshl.de/auth/ldap/ntlmsso_attempt.php");
        webClient.getPage(getPHPCookieURL());
        currentPage = webClient.getPage("https://campusapp01.hshl.de/auth/ldap/ntlmsso_finish.php");
    }

    private String getPHPCookieURL() throws WrongCredentialsException {
        String cookieURL = "";
        try {
            String source = currentPage.asXml();
            int begin = source.indexOf("https://campusapp01.hshl.de/auth/ldap/ntlmsso_magic.php?sesskey=");
            int end = source.indexOf( '\"', begin);
            cookieURL = source.substring(begin, end);
        } catch (StringIndexOutOfBoundsException e) {
            //if the string can't be found, you are not logged in into the campusportal.
            throw new WrongCredentialsException();
        }
        return cookieURL;
    }

    public HtmlPage establishConnection() throws IOException {
        loginToCampusPortal();
        loginToLernplattform();
        return currentPage;
    }

    public WebClient getWebClient() {
        return webClient;
    }
}
