package com.movierecommender.project.dao;
import java.util.List;

import com.google.gson.JsonObject;
import com.movierecommender.project.dto.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Interface defining the Retrofit API calls for interacting with the API
 * This interface contains methods for searching movies and discovering movies
 * based on different criteria.
 *
 * @author Marko Nisiama
 */
public interface IMovieRetrofitDAO {

    @Headers("accept: application/json")
    @GET("/3/search/movie")
    Call<JsonObject> searchMovies(
            @Header("Authorization") String authorization,
            @Query("query") String query,
            @Query("include_adult") boolean includeAdult,
            @Query("language") String language,
            @Query("page") int page
    );

    @Headers("accept: application/json")
    @GET("/3/discover/movie")
    Call<JsonObject> discoverMovies(
            @Header("Authorization") String authorization,
            @Query("include_adult") boolean includeAdult,
            @Query("include_video") boolean includeVideo,
            @Query("language") String language,
            @Query("page") int page,
            @Query("sort_by") String sortBy

    );


}
