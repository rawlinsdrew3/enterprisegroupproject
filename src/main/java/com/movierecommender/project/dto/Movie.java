package com.movierecommender.project.dto;

import com.google.gson.annotations.SerializedName;
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
    @SerializedName("movieID")
    public int movieId;
    @SerializedName("title")
    public String title;
    @SerializedName("genre")
    public String genre;
    @SerializedName("director")
    public String director;
    @SerializedName("rating")
    public int rating;
    @SerializedName("releaseYear")
    public String releaseYear;
    @SerializedName("description")
    public String description;
    @SerializedName("posterUrl")
    private String posterUrl;


    //    public String getPosterUrl() {
//        return posterUrl;
//    }
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
