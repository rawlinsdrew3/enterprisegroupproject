package com.movierecommender.project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("favoriteGenres")
public class ProfileController {

    // Show the profile page
    @GetMapping("/profile")
    public String showProfile(Model model) {
        // Initialize favoriteGenres if not already set
        List<String> favoriteGenres = (List<String>) model.getAttribute("favoriteGenres");
        if (favoriteGenres == null) {
            favoriteGenres = new ArrayList<>();
        }
        model.addAttribute("favoriteGenres", favoriteGenres);
        return "profile";
    }

    // Add a genre to the favoriteGenres list
    @PostMapping("/profile")
    public String addFavoriteGenre(@RequestParam String selectedGenre, Model model) {
        List<String> favoriteGenres = (List<String>) model.getAttribute("favoriteGenres");
        if (favoriteGenres == null) {
            favoriteGenres = new ArrayList<>();
        }

        if (!favoriteGenres.contains(selectedGenre)) {
            favoriteGenres.add(selectedGenre);
        }

        model.addAttribute("favoriteGenres", favoriteGenres);
        return "profile";
    }
}
