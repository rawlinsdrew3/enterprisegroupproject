package com.movierecommender.project.dto;

import lombok.Data;


public @Data class Movie
{
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
