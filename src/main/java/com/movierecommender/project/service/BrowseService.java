package com.movierecommender.project.service;

import com.movierecommender.project.dao.IMovieDAO;
import com.movierecommender.project.dto.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for browsing and filtering movies from an external movie database API.
 * This class interacts with the external API to fetch movie data based on a search query,
 * genre, and rating.
 *
 * @author Marko Nisiama
 */
@Service
public class BrowseService implements IBrowseService {

    private final IMovieDAO movieDAO;

    @Autowired
    public BrowseService(IMovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    /**
     * Fetches a list of movies based on the provided query.
     * If the query is empty or null, it fetches popular movies.
     *
     * @param query The search query string. Can be null or empty to fetch popular movies.
     * @return A list of movie objects.
     * @throws IOException If there is an issue with the API request or response.
     * @throws InterruptedException If the API request is interrupted.
     */
    @Override
    public List<Movie> fetchMovies(String query) throws IOException {
        if (query != null && !query.trim().isEmpty()) {
            return movieDAO.fetchMoviesByQuery(query);
        } else {
            return movieDAO.fetchPopularMovies();
        }
    }

    /**
     * Fetches and filters a list of movies based on the provided query, genre, and rating.
     *
     * @param query The search query string. Can be null or empty to fetch popular movies.
     * @param genre The genre to filter the movies by. Can be null or empty to include all genres.
     * @param rating The rating filter. It can be "highRating", "midRating", or "lowRating".
     * @return A list of filtered movie objects.
     * @throws IOException If there is an issue with the API request or response.
     * @throws InterruptedException If the API request is interrupted.
     */
    @Override
    public List<Movie> fetchAndFilterMovies(String query, String genre, String rating) throws IOException {
        List<Movie> movies = fetchMovies(query);
        return filterMovies(movies, genre, rating);
    }


    @Override
    public List<Movie> filterMovies(List<Movie> movies, String genre, String rating) {
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
