package com.movierecommender.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movierecommender.project.dao.MovieRepository;
import com.movierecommender.project.dto.Movie;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MovieRepository movieRepository;

    private static final String API_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmZmYTM2NjgyYTY5ZDM1OWQ1MjQ4OTFkNDQ1OGI2NiIsIm5iZiI6MTcyOTYyNDQ5MS43MjE2MTgsInN1YiI6IjY3MTdmODBjNmU0MjEwNzgwZjc4NzRlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ubnKxHe4mfL51OE8NhOz31gacsvQU0wyh1Ja6vfp4D0"; // Replace with your actual token

    @Override
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
    public Iterable<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }
}
