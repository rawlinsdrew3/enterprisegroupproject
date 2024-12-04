package com.movierecommender.project;

import com.movierecommender.project.dto.Movie;
import com.movierecommender.project.service.IBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class BrowseController {

    @Autowired
    private IBrowseService browseService;

    @GetMapping("/browse")
    public String browseMovies(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String rating,
            Model model) {
        try {
            // Fetch and filter movies
            List<Movie> movies = browseService.fetchAndFilterMovies(query, genre, rating);

            // Add attributes for Thymeleaf rendering
            model.addAttribute("movies", movies);
            model.addAttribute("query", query);
            model.addAttribute("genre", genre);
            model.addAttribute("rating", rating);

            return "browse";
        } catch (IOException e) {
            // Handle API errors gracefully
            model.addAttribute("error", "Error fetching movies: " + e.getMessage());
            return "browse";
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

