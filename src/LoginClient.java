import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
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

    public LoginClient(String username, String password) {
        this.username = username;
        this.password = password;
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
        // TODO: 16.06.2016 return boolean if login was successful
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
        // TODO: 16.06.2016 return boolean if login was successful
    }

    private String getPHPCookieURL() {
        String source = currentPage.asXml();
        int begin = source.indexOf("https://campusapp01.hshl.de/auth/ldap/ntlmsso_magic.php?sesskey=");
        int end = source.indexOf( '\"', begin);
        String cookieURL = source.substring(begin, end);
        return cookieURL;
    }

    public WebClient establishConnection() {
        try {
            loginToCampusPortal();
            loginToLernplattform();
            System.out.println(currentPage.asText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return webClient;
    }
}
