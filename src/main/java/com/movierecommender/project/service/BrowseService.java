package com.movierecommender.project.service;

import com.movierecommender.project.dao.IMovieDAO;
import com.movierecommender.project.dto.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrowseService implements IBrowseService {

    @Autowired
    private IMovieDAO movieDAO; // Use the DAO for Retrofit-based API calls

    @Override
    public List<Movie> fetchMovies(String query) throws IOException {
        return (query != null && !query.trim().isEmpty())
                ? fetchMoviesByQuery(query)
                : fetchPopularMovies();
    }

    @Override
    public List<Movie> fetchAndFilterMovies(String query, String genre, String rating) throws IOException {
        List<Movie> movies = fetchMovies(query); // Fetch movies
        return filterMovies(movies, genre, rating); // Apply filters
    }

    @Override
    public List<Movie> fetchMoviesByQuery(String query) throws IOException {
        return movieDAO.fetchMoviesByQuery(query);
    }

    @Override
    public List<Movie> fetchPopularMovies() throws IOException {
        return movieDAO.fetchPopularMovies();
    }

    private List<Movie> filterMovies(List<Movie> movies, String genre, String rating) {
        return movies.stream()
                .filter(movie -> genre == null || genre.isEmpty() || movie.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .filter(movie -> {
                    int voteAverage = movie.getRating();
                    if ("highRating".equalsIgnoreCase(rating)) {
                        return voteAverage >= 7 && voteAverage <= 10;
                    } else if ("midRating".equalsIgnoreCase(rating)) {
                        return voteAverage >= 4 && voteAverage < 7;
                    } else if ("lowRating".equalsIgnoreCase(rating)) {
                        return voteAverage >= 1 && voteAverage < 4;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
