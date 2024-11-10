package com.movierecommender.project;

import com.movierecommender.project.dto.Signin;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movie")
    public String getMovie() {
        return movieService.getMovie();
    }

    // HTML page mappings
    @GetMapping("/")
    public String index() {
        return "Index";
    }

    @GetMapping("/login")
    public String showSigninForm(@ModelAttribute("signin") Signin signin) {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @PostMapping("/signin")
    public String processSignin(@Valid @ModelAttribute("signin") Signin signin,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login"; // If errors are present, return to signin page with error messages
        }
        // If no errors, process the login logic (e.g., authentication)
        return "redirect:/home"; // Redirect to home or another page upon successful login
    }
}