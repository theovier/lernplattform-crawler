import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class Main {

    public static void main(String[] args ) {
        Window window = new Window();
    }

    public static void startLogin(LoginCredentials credentials) {
        LoginClient client = new LoginClient(credentials);
        boolean success = false;
        HtmlPage overviewPage = null;
        try {
            overviewPage = client.establishConnection();
            success = true;
        } catch (WrongCredentialsException e) {
            System.out.println("falsche credentials");
        } catch (IOException e) {
            System.out.println("internet probleme?");
        }

        if (success) {
            System.out.println("success");
            PDFGatewayCrawler crawler = new PDFGatewayCrawler(client.getWebClient(), overviewPage);
            //crawler.startDemo();
            crawler.startDemo();
        }

        System.exit(0);
    }

    //get label mit inhalt "Sommersemester 2016" (Sommersemester/Wintersemester XXXX)
    //die nächste ol -> davon die lis -> a  href
}

