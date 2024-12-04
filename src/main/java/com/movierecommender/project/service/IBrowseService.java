package com.movierecommender.project.service;

import com.movierecommender.project.dto.Movie;

import java.io.IOException;
import java.util.List;

public interface IBrowseService {

    /**
     * Fetches a list of movies based on the provided search query.
     * If the query is empty or null, it fetches popular movies.
     *
     * @param query The search query string. Can be null or empty to fetch popular movies.
     * @return A list of movie objects matching the search query.
     * @throws IOException If there is an issue with the API request or response.
     * @throws InterruptedException If the API request is interrupted.
     */
    List<Movie> fetchMovies(String query) throws IOException, InterruptedException;

    /**
     * Fetches and filters a list of movies based on the provided search query, genre, and rating.
     *
     * @param query The search query string. Can be null or empty to fetch popular movies.
     * @param genre The genre to filter the movies by. Can be null or empty to include all genres.
     * @param rating The rating filter. It can be "highRating", "midRating", or "lowRating".
     * @return A filtered list of movie objects matching the search query and filters.
     * @throws IOException If there is an issue with the API request or response.
     * @throws InterruptedException If the API request is interrupted.
     */
    List<Movie> fetchAndFilterMovies(String query, String genre, String rating) throws IOException, InterruptedException;
}
