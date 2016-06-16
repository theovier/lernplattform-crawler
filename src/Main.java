public class Main {

    public static void main(String[] args ) throws Exception {

        String user = "user"; //Vorname.Nachname
        String password = "password";

		Client client = new Client(user, password);
        client.login();
    }
}

