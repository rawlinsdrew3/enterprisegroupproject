package com.movierecommender.project.service;

import com.movierecommender.project.dto.Movie;

import java.io.IOException;
import java.util.List;

public interface IBrowseService {

    List<Movie> fetchMovies(String query) throws IOException, InterruptedException;

    List<Movie> fetchAndFilterMovies(String query, String genre, String rating) throws IOException, InterruptedException;

    List<Movie> filterMovies(List<Movie> movies, String genre, String rating);
}
