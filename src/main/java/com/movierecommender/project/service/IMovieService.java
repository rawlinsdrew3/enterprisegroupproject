package com.movierecommender.project.service;

import com.movierecommender.project.dto.Movie;

import java.io.IOException;
import java.util.Optional;

public interface IMovieService {

    /**
     * Fetch a specific movie from the external API.
     *
     * @param movieId The ID of the movie to fetch.
     * @return The fetched movie object.
     * @throws IOException If an error occurs during the HTTP request.
     * @throws InterruptedException If the HTTP request is interrupted.
     */
    Movie fetchMovieFromExternalApi(int movieId) throws IOException, InterruptedException;

    /**
     * Fetch all movies from the local database.
     *
     * @return An iterable collection of movies.
     */
    Iterable<Movie> getAllMovies();

    /**
     * Save a movie to the local database.
     *
     * @param movie The movie object to save.
     * @return The saved movie object.
     */
    Movie saveMovie(Movie movie);
}
