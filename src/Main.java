public class Main {

    public static void main(String[] args ) {
        Window window = new Window();
    }

    public static void startLogin(String user, String password) {
        LoginClient client = new LoginClient(user, password);
        client.establishConnection();
        PDFCrawler crawler = new PDFCrawler(client.getWebClient());
        crawler.startDemo();
    }

    //class: activity resource modtype_resource  -> id module-86171
    //https://campusapp01.hshl.de/mod/resource/view.php?id=86171

    //window.open (danach folgt der download link)
}

