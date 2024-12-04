package com.movierecommender.project.dao;

import com.google.gson.JsonArray;
import com.movierecommender.project.dto.Movie;

import java.io.IOException;
import java.util.List;

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
    List<Movie> parseMoviesFromJson(JsonArray resultsArray);
    String mapGenres(JsonArray genreIds);
}
