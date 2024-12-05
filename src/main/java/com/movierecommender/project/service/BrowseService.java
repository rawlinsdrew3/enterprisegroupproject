package com.movierecommender.project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movierecommender.project.dto.Movie;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BrowseService implements IBrowseService {

    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmZmYTM2NjgyYTY5ZDM1OWQ1MjQ4OTFkNDQ1OGI2NiIsIm5iZiI6MTcyOTYyNDQ5MS43MjE2MTgsInN1YiI6IjY3MTdmODBjNmU0MjEwNzgwZjc4NzRlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ubnKxHe4mfL51OE8NhOz31gacsvQU0wyh1Ja6vfp4D0"; // Replace with your actual token
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final Map<Integer, String> GENRE_MAP = Map.of(
            28, "Action", 35, "Comedy", 18, "Drama", 27, "Horror",
            10749, "Romance", 16, "Animation", 878, "Science Fiction",
            10751, "Family", 12, "Adventure", 53, "Thriller"
    );

    @Override
    public List<Movie> fetchMovies(String query) throws IOException, InterruptedException {
        String url = query != null && !query.trim().isEmpty()
                ? BASE_URL + "/search/movie?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&include_adult=false&language=en-US&page=1"
                : BASE_URL + "/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";

        return fetchMoviesFromApi(url);
    }

    @Override
    public List<Movie> fetchAndFilterMovies(String query, String genre, String rating) throws IOException, InterruptedException {
        List<Movie> movies = fetchMovies(query);
        return filterMovies(movies, genre, rating);
    }

    private List<Movie> fetchMoviesFromApi(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return parseMovies(response.body());
        } else {
            throw new RuntimeException("Failed to fetch movies. Status code: " + response.statusCode());
        }
    }

    private List<Movie> parseMovies(String jsonResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode resultNode = rootNode.get("results");

        List<Movie> movies = new ArrayList<>();
        for (JsonNode movieNode : resultNode) {
            Movie movie = new Movie();
            movie.setMovieId(movieNode.path("id").asInt());
            movie.setTitle(movieNode.path("original_title").asText());
            movie.setPosterUrl("https://image.tmdb.org/t/p/original" + movieNode.path("poster_path").asText());

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
        return movies;
    }

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
