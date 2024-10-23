import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

@RestController
public class MovieController {

    @GetMapping("/movie")
    public String getMovie() {
        String url = "https://api.themoviedb.org/3/movie/24?language=en-US";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmZmYTM2NjgyYTY5ZDM1OWQ1MjQ4OTFkNDQ1OGI2NiIsIm5iZiI6MTcyOTYyNDQ5MS43MjE2MTgsInN1YiI6IjY3MTdmODBjNmU0MjEwNzgwZjc4NzRlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ubnKxHe4mfL51OE8NhOz31gacsvQU0wyh1Ja6vfp4D0") // Replace with your actual token
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return "Error: " + response.statusCode();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error occurred while fetching movie data.";
        }
    }

    //Added mapping for html pages
    @RequestMapping("/")
    public String Index()
    {
        return "Index";
    }
    @RequestMapping("/Login")
    public String Login()
    {
        return "login";
    }
    @RequestMapping("/Signup")
    public String Signup()
    {
        return "signup";
    }
    @RequestMapping("/Browse")
    public String Browse()
    {
        return "browse";
    }
    @RequestMapping("/Profile")
    public String Profile()
    {
        return "profile";
    }
}
