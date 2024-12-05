package com.movierecommender.project.dao;

import com.google.gson.JsonArray;
import com.movierecommender.project.dto.Movie;

import java.io.IOException;
import java.util.List;

/**
 * Interface for interacting with movie data from a movie database.
 * This interface defines methods for fetching movies based on search queries
 * and popularity, parsing movies from JSON, and mapping genres.
 *
 * @author Marko Nisiama
 */
public interface IMovieDAO {

    /**
     * Fetch a list of movies based on a search query.
     *
     * @param query The search keyword.
     * @return A list of movies matching the query.
     * @throws IOException If an error occurs during the HTTP request.
     */
    List<Movie> fetchMoviesByQuery(String query) throws IOException;

    /**
     * Fetch a list of popular movies.
     *
     * @return A list of popular movies.
     * @throws IOException If an error occurs during the HTTP request.
     */
    List<Movie> fetchPopularMovies() throws IOException;

    /**
     * Parses a JsonArray of movie results from a JSON response and converts it into a list of movie objects.
     *
     * @param resultsArray The JsonArray containing movie data returned by the external movie database API.
     * @return A list of movie objects populated with data parsed from the JSON response.
     */
    List<Movie> parseMoviesFromJson(JsonArray resultsArray);

    /**
     * Maps a list of genre IDs to their corresponding genre names.
     *
     * @param genreIds A JsonArray of genre IDs for a movie.
     * @return A comma-separated String of genre names corresponding to the provided genre IDs.
     */
    String mapGenres(JsonArray genreIds);
}
