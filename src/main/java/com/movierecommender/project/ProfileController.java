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
public class ProfileController
{

    //Initialization
    @GetMapping("/profile")
    public String showProfile(Model model)
    {
        List<String> favoriteGenres = (List<String>) model.getAttribute("favoriteGenres");

        if (favoriteGenres == null)
        {
            favoriteGenres = new ArrayList<>();
        }

        model.addAttribute("favoriteGenres", favoriteGenres);

        return "profile";
    }

    // Appending Genres
    @PostMapping("/profile")
    public String addFavoriteGenre(@RequestParam String selectedGenre, Model model)
    {
        List<String> favoriteGenres = (List<String>) model.getAttribute("favoriteGenres");

        if (favoriteGenres == null)
        {
            favoriteGenres = new ArrayList<>();
        }

        // Prevent adding duplicates
        if (!favoriteGenres.contains(selectedGenre))
        {
            favoriteGenres.add(selectedGenre);
        }

        model.addAttribute("favoriteGenres", favoriteGenres);

        return "/profile";
    }
}

//Note: Selected genres seem to disappear when leaving the profile page and coming back.
//If this does happen pressing add genre on an already existing genre should update the page to include the selected genres
//Could be an issue to fix in future commits
