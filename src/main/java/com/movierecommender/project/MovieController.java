package com.movierecommender.project;
import com.movierecommender.project.dto.Movie;
import com.movierecommender.project.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedWriter;
import java.io.FileWriter;


@Controller
public class MovieController {
    @Autowired
    IMovieService movieService;

    // Fetch a specific movie from the external API
    @GetMapping("/movie")
    @ResponseBody
    public ResponseEntity<?> getMovieFromApi() {
        try {
            Movie movie = movieService.fetchMovieFromExternalApi(24); // Example movie ID
            return ResponseEntity.ok(movie);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching movie data: " + e.getMessage());
        }
    }
    @GetMapping("/movies")
    @ResponseBody
    public ResponseEntity<?> getAllMovies() {
        try {
            Iterable<Movie> movies = movieService.getAllMovies();
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching movies: " + e.getMessage());
        }
    }

    // Add a new movie to the local database
    @PostMapping("/movies")
    @ResponseBody
    public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        try {
            Movie savedMovie = movieService.saveMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving the movie: " + e.getMessage());
        }
    }

    @PostMapping("/submit-rating")
    public String submitRating(@RequestParam String movieTitle, @RequestParam int movieRating)
    {
        String fileName = "user_ratings.txt";
        String ratedMovie = "Movie: " + movieTitle + ", Rating: " + movieRating + "\n";

        try (FileWriter fileWriter = new FileWriter(fileName, true);
             BufferedWriter ratingWriter = new BufferedWriter(fileWriter))
        {
            ratingWriter.write(ratedMovie);
        } catch (Exception e)
        {
            e.printStackTrace();
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
}
