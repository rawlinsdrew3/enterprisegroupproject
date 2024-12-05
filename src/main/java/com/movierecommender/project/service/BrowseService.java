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

/**
 * Service class for browsing and filtering movies from an external movie database API.
 * This class interacts with the external API to fetch movie data based on a search query,
 * genre, and rating.
 *
 * @author Marko Nisiama
 */
@Service
public class BrowseService implements IBrowseService {

    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmZmYTM2NjgyYTY5ZDM1OWQ1MjQ4OTFkNDQ1OGI2NiIsIm5iZiI6MTcyOTYyNDQ5MS43MjE2MTgsInN1YiI6IjY3MTdmODBjNmU0MjEwNzgwZjc4NzRlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ubnKxHe4mfL51OE8NhOz31gacsvQU0wyh1Ja6vfp4D0"; // Replace with your actual token
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final Map<Integer, String> GENRE_MAP = Map.of(
            28, "Action", 35, "Comedy", 18, "Drama", 27, "Horror",
            10749, "Romance", 16, "Animation", 878, "Science Fiction",
            10751, "Family", 12, "Adventure", 53, "Thriller"
    );

    /**
     * Fetches a list of movies based on the provided query.
     * If the query is empty or null, it fetches popular movies.
     *
     * @param query The search query string. Can be null or empty to fetch popular movies.
     * @return A list of movie objects.
     * @throws IOException If there is an issue with the API request or response.
     * @throws InterruptedException If the API request is interrupted.
     */
    @Override
    public List<Movie> fetchMovies(String query) throws IOException, InterruptedException {
        String url = query != null && !query.trim().isEmpty()
                ? BASE_URL + "/search/movie?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&include_adult=false&language=en-US&page=1"
                : BASE_URL + "/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";

        return fetchMoviesFromApi(url);
    }

    /**
     * Fetches and filters a list of movies based on the provided query, genre, and rating.
     *
     * @param query The search query string. Can be null or empty to fetch popular movies.
     * @param genre The genre to filter the movies by. Can be null or empty to include all genres.
     * @param rating The rating filter. It can be "highRating", "midRating", or "lowRating".
     * @return A list of filtered movie objects.
     * @throws IOException If there is an issue with the API request or response.
     * @throws InterruptedException If the API request is interrupted.
     */
    @Override
    public List<Movie> fetchAndFilterMovies(String query, String genre, String rating) throws IOException, InterruptedException {
        List<Movie> movies = fetchMovies(query);
        return filterMovies(movies, genre, rating);
    }

    /**
     * Sends an API request to fetch movies from the movie database based on the provided URL.
     *
     * @param url The URL to fetch the movies from.
     * @return A list of movie objects.
     * @throws IOException If there is an issue with the API request or response.
     * @throws InterruptedException If the API request is interrupted.
     */
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

    /**
     * Parses the JSON response from the API and converts it into a list of movie objects.
     *
     * @param jsonResponse The JSON response as a string.
     * @return A list of movie objects parsed from the response.
     * @throws IOException If there is an issue with parsing the JSON response.
     */
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

    /**
     * Filters the list of movies based on the provided genre and rating.
     *
     * @param movies The list of movies to filter.
     * @param genre The genre to filter by. Can be null or empty to include all genres.
     * @param rating The rating filter. It can be "highRating", "midRating", or "lowRating".
     * @return A filtered list of movie objects.
     */
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