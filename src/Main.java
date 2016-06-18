import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

public class Main {

    public static void main(String[] args ) {

        String user = "Theo.Harkenbusch"; //Vorname.Nachname
        String password = "password"; //old

		LoginClient client = new LoginClient(user, password);
        client.establishConnection();
        PDFCrawler crawler = new PDFCrawler(client.getWebClient());
        crawler.startDemo();
    }

    //class: activity resource modtype_resource  -> id module-86171
    //https://campusapp01.hshl.de/mod/resource/view.php?id=86171

    //window.open (danach folgt der download link)
}

