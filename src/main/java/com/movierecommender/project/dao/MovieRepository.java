package com.movierecommender.project.dao;

import com.movierecommender.project.dto.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing and managing `Movie` entities in the database.
 * This interface also includes custom methods for searching movies by genre and title.
 *
 * @author Marko Nisiama
 */
@Repository

public interface MovieRepository extends CrudRepository<Movie, Integer> {
    List<Movie> findByGenre(String genre);
    List<Movie> findByTitleContaining(String keyword);
}
