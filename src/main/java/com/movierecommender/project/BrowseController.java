package com.movierecommender.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.movierecommender.project.dto.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
//import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BrowseController {

    @GetMapping("/browse")
    //method accounts for search queries with @RequestParam
    public String browseMovies(@RequestParam(required = false) String query, Model model) {

        //url is set to an empty string
        String url;
        //here, if there is a search the database will be queried and populate with relevant material
        // else will return nothing

        if (query != null&& !query.trim().isEmpty()) {
            url = "https://api.themoviedb.org/3/search/movie?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&include_adult=false&language=en-US&page=1";
        }
        // uncomment the url and comment the return to return a default set of movies
        else {
            //url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";
            return "browse";
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmZmYTM2NjgyYTY5ZDM1OWQ1MjQ4OTFkNDQ1OGI2NiIsIm5iZiI6MTcyOTYyNDQ5MS43MjE2MTgsInN1YiI6IjY3MTdmODBjNmU0MjEwNzgwZjc4NzRlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ubnKxHe4mfL51OE8NhOz31gacsvQU0wyh1Ja6vfp4D0") //api token
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //pings database and sends data
            if (response.statusCode() == 200) {
                List<Movie> movies = parseMovies(response.body());
                //code for searchbox
                if (query !=null && !query.trim().isEmpty()){
                    movies = movies.stream()
                            .filter(movie -> movie.getTitle().toLowerCase().contains(query.toLowerCase()))
                            .toList();
                }

                model.addAttribute("movies", movies);
                return "browse";
            } else {
                model.addAttribute("error", "Something went wrong" + response.statusCode());
                return "browse";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error Occurred While Fetching Data");
            return "browse";
        }
    }
// list used to populate with movie api data
    private List<Movie> parseMovies(String jsonResponse) throws JsonProcessingException {

        List<Movie> movies = new ArrayList<>();
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            JsonNode resultNode = rootNode.get("results");
//gets api data - uses dto to determine type - then is sent to html for display ---- RATING(vote_average) NOT WORKING CURRENTLY
            for (JsonNode movieNode : resultNode) {
                Movie movie = new Movie();
                movie.setMovieId(movieNode.path("id").asInt());
                movie.setTitle(movieNode.path("original_title").asText());
                movie.setGenre(movieNode.path("genre_ids").asText());
                movie.setRating(movieNode.path("vote_average").asInt());
                movie.setReleaseYear(movieNode.path("release_date").asText());
                movie.setDescription(movieNode.path("overview").asText());
                movies.add(movie);

            }
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return movies;
    }
}
