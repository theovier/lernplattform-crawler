import java.net.URL;

public class Main {

    public static void main(String[] args ) throws Exception {

        String user = "Theo.Harkenbusch"; //Vorname.Nachname
        String password = "Wasserbett3";

		Client client = new Client(user, password);
        client.login();
    }

    //class: activity resource modtype_resource  -> id module-86171
    //https://campusapp01.hshl.de/mod/resource/view.php?id=86171

    //window.open (danach folgt der download link)
}

