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
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class BrowseController {

    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmZmYTM2NjgyYTY5ZDM1OWQ1MjQ4OTFkNDQ1OGI2NiIsIm5iZiI6MTcyOTYyNDQ5MS43MjE2MTgsInN1YiI6IjY3MTdmODBjNmU0MjEwNzgwZjc4NzRlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ubnKxHe4mfL51OE8NhOz31gacsvQU0wyh1Ja6vfp4D0"; // Replace with your actual token
    private static final Map<Integer, String> GENRE_MAP = Map.of(
            28, "Action",
            35, "Comedy",
            18, "Drama",
            27, "Horror",
            10749, "Romance",
            16, "Animation",
            878, "Science Fiction",
            10751, "Family",
            12, "Adventure",
            53, "Thriller"
            // Add more mappings as needed.
    );

    @GetMapping("/browse")
    public String browseMovies(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String rating,
            Model model) {

        //url is set to an empty string
        String url;
        //here, if there is a search the database will be queried and populate with relevant material
        // else will return nothing

        if (query != null && !query.trim().isEmpty()) {
            url = "https://api.themoviedb.org/3/search/movie?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&include_adult=false&language=en-US&page=1";
        }
        // uncomment the url and comment the return to return a default set of movies
        else {
            url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + BEARER_TOKEN) //api token
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //pings database and sends data
            if (response.statusCode() == 200) {
                List<Movie> movies = parseMovies(response.body());
                // Apply filters if necessary
                if (query != null && !query.trim().isEmpty()){
                    movies = movies.stream()
                            .filter(movie -> movie.getTitle().toLowerCase().contains(query.toLowerCase()))
                            .collect(Collectors.toList());
                }

                // Apply additional filters if genre or rating are provided
                if (genre != null || rating != null) {
                    movies = filterMovies(movies, genre, rating);
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

//entirely unsure why this no longer works, think it is because it is a duplicate client request from elsewhere. - Earl
//        client = HttpClient.newHttpClient();
//        request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .header("accept", "application/json")
//                .header("Authorization", "Bearer " + BEARER_TOKEN) //api token
//                .GET()
//                .build();
//
//        try {
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            //pings database and sends data
//            if (response.statusCode() == 200) {
//                List<Movie> movies = parseMovies(response.body());
//                //code for searchbox
//                if (query !=null && !query.trim().isEmpty()){
//                    movies = movies.stream()
//                            .filter(movie -> movie.getTitle().toLowerCase().contains(query.toLowerCase()))
//                            .toList();
//                }
//
//
//                model.addAttribute("movies", movies);
//                return "browse";
//            } else {
//                model.addAttribute("error", "Something went wrong" + response.statusCode());
//                return "browse";
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            model.addAttribute("error", "Error Occurred While Fetching Data");
//            return "browse";
//        }
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

                //String posterDisplay = movieNode.path("poster_display").asText();
                String posterUrl = "https://image.tmdb.org/t/p/original" + movieNode.path("poster_path").asText();

                movie.setPosterUrl(posterUrl);

                List<Integer> genreIds = mapper.convertValue(movieNode.path("genre_ids"), List.class);
                String genres = genreIds.stream()
                        .map(id -> GENRE_MAP.getOrDefault(id, "Unknown"))
                        .collect(Collectors.joining(", "));
                movie.setGenre(genres);

                movie.setRating((int) Math.round(movieNode.path("vote_average").asDouble()));
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
    @GetMapping("/browse/filtered")
    public String browseMoviesWithFilters(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String rating,
            Model model) {

        // If no query is provided, return the view with no results.
        if (query == null || query.trim().isEmpty()) {
            return "browse";
        }

        // Construct the URL for searching movies by name.
        String url = "https://api.themoviedb.org/3/search/movie?query=" +
                URLEncoder.encode(query, StandardCharsets.UTF_8) +
                "&include_adult=false&language=en-US&page=1";

        // Fetch movies from the API.
        List<Movie> movies = fetchMoviesFromApi(url);

        // Apply filters if movies are fetched.
        if (movies != null && !movies.isEmpty()) {
            movies = filterMovies(movies, genre, rating);
        }

        model.addAttribute("movies", movies);
        return "browse";
    }


    // Helper method to fetch movies from the API.
    private List<Movie> fetchMoviesFromApi(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return parseMovies(response.body());
            } else {
                System.out.println("Error: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Method to filter movies based on genre and rating.
    private List<Movie> filterMovies(List<Movie> movies, String genre, String rating) {
        return movies.stream()
                .filter(movie -> genre == null || genre.isEmpty() || movie.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .filter(movie -> {
                    int voteAverage = movie.getRating();
                    if ("highRating".equalsIgnoreCase(rating)) {
                        return voteAverage >= 7 && voteAverage <= 10;
                    } else if ("midRating".equalsIgnoreCase(rating)) {
                        return voteAverage >= 4 && voteAverage < 7;
                    } else if ("lowRating".equalsIgnoreCase(rating)) {
                        return voteAverage >= 1 && voteAverage < 4;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}


