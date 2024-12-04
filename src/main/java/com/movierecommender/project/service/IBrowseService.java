package com.movierecommender.project.service;

import com.movierecommender.project.dto.Movie;

import java.io.IOException;
import java.util.List;

public interface IBrowseService {

    List<Movie> fetchMovies(String query) throws IOException, InterruptedException;

    List<Movie> fetchAndFilterMovies(String query, String genre, String rating) throws IOException, InterruptedException;

    List<Movie> fetchMoviesByQuery(String query) throws IOException;
    List<Movie> fetchPopularMovies() throws IOException;

}
