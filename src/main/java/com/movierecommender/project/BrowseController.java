package com.movierecommender.project;

import com.movierecommender.project.dto.Movie;
import com.movierecommender.project.service.IBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

/**
 * Controller class responsible for handling movie browsing operations.
 * Provides endpoints to search and filter movies by title, genre, and rating.
 *
 * @author Marko Nisiama, Earl Schreck
 */
@Controller
public class BrowseController {

    private final IBrowseService browseService;

    @Autowired
    public BrowseController(IBrowseService browseService) {
        this.browseService = browseService;
    }

    /**
     * Handles HTTP GET requests to browse movies.
     *
     * @param query an optional search query string to filter movies by their titles
     * @param genre an optional genre to filter movies by their genre
     * @param rating an optional rating to filter movies by their rating
     * @param model the Model object to pass attributes to the view
     * @return the name of the Thymeleaf template to render, which is "browse"
     */
    @GetMapping("/browse")
    public String browseMovies(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String rating,
            Model model) {
        try {
            List<Movie> movies = browseService.fetchAndFilterMovies(query, genre, rating);
            model.addAttribute("movies", movies);
            model.addAttribute("query", query);
            model.addAttribute("genre", genre);
            model.addAttribute("rating", rating);
            return "browse";
        } catch (IOException | InterruptedException e) {
            model.addAttribute("error", "Error occurred while browsing movies: " + e.getMessage());
            return "browse";
        }
    }

    /**
     * Handles HTTP GET requests to browse movies with optional filters such as query, genre, and rating.
     *
     * @param query an optional search query string to filter movies by their titles
     * @param genre an optional genre to filter movies by their genre
     * @param rating an optional rating to filter movies by their rating
     * @return a ResponseEntity containing a list of filtered movies if successful; otherwise,
     * returns an internal server error with an error message
     */
    @GetMapping("/filtered")
    public ResponseEntity<?> browseMoviesWithFilters(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String rating) {
        try {
            List<Movie> movies = browseService.fetchAndFilterMovies(query, genre, rating);
            return ResponseEntity.ok(movies);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while filtering movies: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


