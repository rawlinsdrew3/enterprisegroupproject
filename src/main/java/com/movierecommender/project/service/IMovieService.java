package com.movierecommender.project.service;

import com.movierecommender.project.dto.Movie;

import java.io.IOException;
import java.util.Optional;

public interface IMovieService {

    /**
     * Fetches a movie from the external API.
     *
     * @param id The ID of the movie to fetch.
     * @return The Movie object fetched from the API.
     * @throws IOException If there is an issue with the HTTP request.
     * @throws InterruptedException If the thread is interrupted during the HTTP request.
     */
    Movie fetchMovieFromExternalApi(int id) throws IOException, InterruptedException;

    /**
     * Saves a movie to the local database.
     *
     * @param movie The Movie object to save.
     * @return The saved Movie object.
     */
    Movie saveMovie(Movie movie);

    /**
     * Retrieves all movies from the local database.
     *
     * @return An Iterable of all Movie objects.
     */
    Iterable<Movie> getAllMovies();

    /**
     * Retrieves a movie by its ID from the local database.
     *
     * @param id The ID of the movie to retrieve.
     * @return An Optional containing the Movie object if found, or empty if not.
     */
    Optional<Movie> getMovieById(int id);
}
