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

/**
 * Service class that provides operations related to movies.
 * The class interacts with the external movie API (TheMovieDB) to fetch movie data,
 * as well as with the local movie repository for storing and retrieving movie data.
 *
 * @author Marko Nisiama
 */
@Service
public class MovieService implements IMovieService {

    @Autowired
    private MovieRepository movieRepository;

    private static final String API_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmZmYTM2NjgyYTY5ZDM1OWQ1MjQ4OTFkNDQ1OGI2NiIsIm5iZiI6MTcyOTYyNDQ5MS43MjE2MTgsInN1YiI6IjY3MTdmODBjNmU0MjEwNzgwZjc4NzRlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ubnKxHe4mfL51OE8NhOz31gacsvQU0wyh1Ja6vfp4D0"; // Replace with your actual token

    /**
     * Fetches a movie from an external API using the movie ID.
     * The movie data is returned as a movie object.
     *
     * @param movieId The unique identifier of the movie to fetch.
     * @return A movie object containing the movie details.
     * @throws IOException If there is an issue with the API request or response.
     * @throws InterruptedException If the API request is interrupted.
     */
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

    /**
     * Retrieves all movies stored in the repository.
     *
     * @return A collection of all movie objects in the repository.
     */
    @Override
    public Iterable<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    /**
     * Saves a movie in the repository.
     * If the movie already exists, it will update the existing record.
     *
     * @param movie The movie object to save.
     * @return The saved movie object.
     */
    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }
}
