package com.movierecommender.project;

import com.movierecommender.project.dto.Movie;
import com.movierecommender.project.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;

@Controller
public class MovieController {

    @Autowired
    private IMovieService movieService;

    // Fetch a specific movie from the external API
    @GetMapping("/movie")
    @ResponseBody
    public ResponseEntity<?> getMovieFromApi(@RequestParam int movieId) {
        try {
            Movie movie = movieService.fetchMovieFromExternalApi(movieId);
            return ResponseEntity.ok(movie);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error while fetching movie: " + e.getMessage());
        }
    }

    // Fetch all movies
    @GetMapping("/movies")
    @ResponseBody
    public ResponseEntity<?> getAllMovies() {
        try {
            return ResponseEntity.ok(movieService.getAllMovies());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error while fetching movies: " + e.getMessage());
        }
    }

    // Add a new movie
    @PostMapping("/movies")
    @ResponseBody
    public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        try {
            return ResponseEntity.ok(movieService.saveMovie(movie));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error while saving movie: " + e.getMessage());
        }
    }

    // Submit movie rating
    @PostMapping("/submit-rating")
    public String submitRating(@RequestParam String movieTitle, @RequestParam int movieRating) {
        String fileName = "user_ratings.txt";
        String ratingEntry = "Movie: " + movieTitle + ", Rating: " + movieRating + "\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(ratingEntry);
        } catch (Exception e) {
            System.err.println("Error writing rating: " + e.getMessage());
        }

        return "redirect:/browse";
    }

    // HTML page mappings
    @GetMapping("/")
    public String index() {
        return "Index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/user-profile")
    public String userProfile() {
        return "profile";
    }
}
