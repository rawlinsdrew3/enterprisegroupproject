package com.movierecommender.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movierecommender.project.dao.MovieRepository;
import com.movierecommender.project.dto.Movie;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService implements IMovieService {

    private final MovieRepository movieRepository;
    private static final String API_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "YOUR_API_KEY"; // Replace with actual API key

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie fetchMovieFromExternalApi(int id) throws IOException, InterruptedException {
        String url = API_URL + id + "?language=en-US";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("Authorization", "Bearer YOUR_ACCESS_TOKEN") // Replace with actual token
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            // Map JSON response to Movie object
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), Movie.class);
        } else {
            throw new RuntimeException("Failed to fetch movie from API: " + response.statusCode());
        }
    }

    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Iterable<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Optional<Movie> getMovieById(int id) {
        return movieRepository.findById(id);
    }
}
