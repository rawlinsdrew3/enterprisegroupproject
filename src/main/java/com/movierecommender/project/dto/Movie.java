package com.movierecommender.project.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * DTO for the Movie Recommender.
 * This class is used to store and retrieve movie data, including movie details
 * like the title, genre, director, rating, release year, description, and poster URL.
 *
 * @author Marko Nisiama, Earl Schreck, Ethan Beach
 */
@Entity
public @Data class Movie
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int movieId;
    public String title;
    public String genre;
    public String director;
    public int rating;
    public String releaseYear;
    public String description;
    private String posterUrl;

//    public String getPosterUrl() {
//        return posterUrl;
//    }
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
