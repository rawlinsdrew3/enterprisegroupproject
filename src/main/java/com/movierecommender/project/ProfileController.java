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

    /**
     * Handles the HTTP GET request for displaying the user's profile page.
     * Initializes the user's list of favorite genres if not already present
     * and adds it to the model attribute.
     *
     * @param model the model object used to pass attributes to the view
     * @return the name of the view to be resolved, which is "profile"
     */
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

    /**
     * Handles the addition of a selected genre to the user's list of favorite genres.
     * If the genre is not already in the list, it is added. The updated list is
     * stored in the model object to be accessible in the user's profile view.
     *
     * @param selectedGenre the genre selected by the user to be added to their list of favorites
     * @param model the model object used to pass attributes to the view
     * @return the name of the view to be resolved, which is "profile"
     */
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
