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
import java.util.List;

@Controller
public class BrowseController {

    @Autowired
    private IBrowseService browseService;

    @GetMapping("/browse")
    //method accounts for search queries with @RequestParam
    public String browseMovies(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String rating,
            Model model) {
        try {
            // Fetch movies with optional filters
            List<Movie> movies = browseService.fetchAndFilterMovies(query, genre, rating);

            // Pass data to the view
            model.addAttribute("movies", movies);
            model.addAttribute("query", query);
            model.addAttribute("genre", genre);
            model.addAttribute("rating", rating);

            return "browse"; // Render Thymeleaf template
        } catch (Exception e) {
            // Handle errors and display an error message
            model.addAttribute("error", "Error occurred while browsing movies: " + e.getMessage());
            return "browse"; // Render Thymeleaf template with error
        }
    }
// list used to populate with movie api data
@GetMapping("/filtered")
public ResponseEntity<?> browseMoviesWithFilters(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) String genre,
        @RequestParam(required = false) String rating) {
    try {
        List<Movie> movies = browseService.fetchAndFilterMovies(query, genre, rating);
        return ResponseEntity.ok(movies);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error occurred while filtering movies: " + e.getMessage());
    }
}
}


