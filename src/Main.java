import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class Main {

    public static void main(String[] args) throws IOException {

        Response res = Jsoup
                .connect("https://campusportal.hshl.de")
                .data("user_name", "user") //replace
                .data("password", "password") //replace
                .data("hshl_domain", "@stud.hshl.de")
                .data("repository", "stud.hshl.de")
                .data("site_name", "campusportal")
                .data("secure", "1")
                .data("resource_id", "2")
                .data("login_type", "2")
                .method(Method.POST)
                .execute();

        System.out.println (res.body());

        //This will get you cookies
        Map<String, String> loginCookies = res.cookies();
    }
}
