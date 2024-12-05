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

/**
 * Controller class responsible for handling movie-related operations.
 * Provides endpoints to fetch movie details from an external API, retrieve
 * movies from the local database, add new movies to the database, and submit
 * user ratings for movies.
 *
 * @author Marko Nisiama, Earl Schreck, Drew Rawlins, Ethan Beach
 */
@Controller
public class MovieController {
    @Autowired
    IMovieService movieService;

    // Fetch a specific movie from the external API

    /**
     * Retrieves a movie from an external API using a predefined movie ID.
     *
     * @return ResponseEntity containing the movie data if successful,
     * or an error message with HTTP status if an exception occurs.
     */
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

    /**
     * Retrieves a list of all movies from the local database.
     *
     * @return ResponseEntity containing Movie objects if successful,
     * or an error message with HTTP status if an exception occurs.
     */
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

    /**
     * Adds a new movie to the local database.
     *
     * @param movie The Movie object containing the details of the movie to be added.
     * @return ResponseEntity containing the saved Movie object with HTTP status CREATED if successful,
     * or an error message with HTTP status INTERNAL_SERVER_ERROR if an exception occurs.
     */
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

    /**
     * Submits a user rating for a specified movie and appends it to a text file.
     *
     * @param movieTitle The title of the movie being rated.
     * @param movieRating The rating given to the movie by the user.
     * @return A string that indicates the redirection path to the browse page.
     */
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
    public String login() {return "login";}

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/Profile")
    public String Profile(){ return "profile"; }
}
