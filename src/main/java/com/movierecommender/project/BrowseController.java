package com.movierecommender.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.movierecommender.project.dto.Movie;
import com.movierecommender.project.service.IBrowseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
//import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class BrowseController {

    @Autowired
    private IBrowseService browseService;

    @GetMapping("/browse")
    //method accounts for search queries with @RequestParam
    public ResponseEntity<?> browseMovies(@RequestParam(required = false) String query) {
        try {
            List<Movie> movies = browseService.fetchMovies(query);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while browsing movies: " + e.getMessage());
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


