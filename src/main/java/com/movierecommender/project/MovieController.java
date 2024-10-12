package com.movierecommender.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MovieController
{
    @RequestMapping("/")
    public String Index()
    {
        return "Index";
    }
}
