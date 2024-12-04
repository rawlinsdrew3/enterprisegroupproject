package com.movierecommender.project.dao;
import java.util.List;
import com.movierecommender.project.dto.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface IMovieRetrofitDAO {
    static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmZmYTM2NjgyYTY5ZDM1OWQ1MjQ4OTFkNDQ1OGI2NiIsIm5iZiI6MTcyOTYyNDQ5MS43MjE2MTgsInN1YiI6IjY3MTdmODBjNmU0MjEwNzgwZjc4NzRlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ubnKxHe4mfL51OE8NhOz31gacsvQU0wyh1Ja6vfp4D0";

    @Headers("api-key:" +  API_KEY)
    @GET("/3/search/movie")
    Call<List<Movie>> searchMovies(
            @Query("query") String query,
            @Query("include_adult") boolean includeAdult,
            @Query("language") String language,
            @Query("page") int page
    );

    @Headers("api-key:" + API_KEY)
    @GET("/3/discover/movie")
    Call<List<Movie>> discoverMovies(
            @Query("include_adult") boolean includeAdult,
            @Query("include_video") boolean includeVideo,
            @Query("language") String language,
            @Query("page") int page,
            @Query("sort_by") String sortBy
    );


}
