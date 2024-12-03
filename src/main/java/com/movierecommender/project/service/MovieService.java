package com.movierecommender.project.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movierecommender.project.dao.MovieRepository;
import com.movierecommender.project.dto.Movie;

@Service
public class MovieService implements IMovieService {

    @Autowired
    private MovieRepository movieRepository;

    private static final String API_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BEARER_TOKEN = "your_bearer_token"; // Replace with your actual token

    @Override
    @Cacheable(value = "movies", key = "#movieId")
    public Movie fetchMovieFromExternalApi(int movieId) throws IOException, InterruptedException {
        String url = API_URL + movieId + "?language=en-US";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), Movie.class);
        } else {
            throw new RuntimeException("Failed to fetch movie from API. Status code: " + response.statusCode());
        }
    }

    @Override
    @Cacheable("moviesList")
    public Iterable<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    @CacheEvict(value = {"movies", "moviesList"}, allEntries = true)
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }
}
