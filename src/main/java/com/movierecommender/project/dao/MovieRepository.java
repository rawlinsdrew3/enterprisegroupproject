package com.movierecommender.project.dao;

import com.movierecommender.project.dto.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MovieRepository extends CrudRepository<Movie, Integer> {
    List<Movie> findByGenre(String genre);
    List<Movie> findByTitleContaining(String keyword);
}
