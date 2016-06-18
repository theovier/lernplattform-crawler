import java.io.IOException;

public class Main {

    public static void main(String[] args ) {
        Window window = new Window();
    }

    public static void startLogin(LoginCredentials credentials) {
        LoginClient client = new LoginClient(credentials);
        boolean success = false;

        try {
            client.establishConnection();
            success = true;
        } catch (WrongCredentialsException e) {
            System.out.println("falsche credentials");
        } catch (IOException e) {
            System.out.println("internet probleme?");
        }

        if (success) {
            System.out.println("success");
            //PDFCrawler crawler = new PDFCrawler(client.getWebClient());
            //crawler.startDemo();
        }
    }

    //class: activity resource modtype_resource  -> id module-86171
    //https://campusapp01.hshl.de/mod/resource/view.php?id=86171

    //window.open (danach folgt der download link)
}

