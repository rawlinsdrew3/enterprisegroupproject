package com.movierecommender.project.dao;

import com.movierecommender.project.dto.Movie;
import org.springframework.stereotype.Repository;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.util.List;


@Repository
public class MovieDAO implements IMovieDAO {

    private static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmZmYTM2NjgyYTY5ZDM1OWQ1MjQ4OTFkNDQ1OGI2NiIsIm5iZiI6MTcyOTYyNDQ5MS43MjE2MTgsInN1YiI6IjY3MTdmODBjNmU0MjEwNzgwZjc4NzRlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ubnKxHe4mfL51OE8NhOz31gacsvQU0wyh1Ja6vfp4D0";

    /**
     * Fetch movies based on a search query.
     *
     * @param query The search keyword.
     * @return A list of movies matching the query.
     * @throws IOException If an error occurs during the HTTP request.
     */
    public List<Movie> fetchMoviesByQuery(String query) throws IOException {
        Retrofit retrofitInstance = RetrofitClientInstance.getRetrofitInstance();

        String authorizationHeader = "Bearer " + API_KEY;

        IMovieRetrofitDAO movieRetrofitDAO = retrofitInstance.create(IMovieRetrofitDAO.class);

        Call<List<Movie>> call = movieRetrofitDAO.searchMovies(authorizationHeader, query, false, "en-US", 1);
        Response<List<Movie>> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch movies: " + response.errorBody().string());
        }
    }

    /**
     * Discover popular movies.
     *
     * @return A list of popular movies.
     * @throws IOException If an error occurs during the HTTP request.
     */
    public List<Movie> fetchPopularMovies() throws IOException {
        Retrofit retrofitInstance = RetrofitClientInstance.getRetrofitInstance();
        IMovieRetrofitDAO movieRetrofitDAO = retrofitInstance.create(IMovieRetrofitDAO.class);
        String authorizationHeader = "Bearer " + API_KEY;

        // Make the API call
        Call<List<Movie>> call = movieRetrofitDAO.discoverMovies(authorizationHeader,false, false, "en-US", 1, "popularity.desc");
        Response<List<Movie>> response = call.execute();

        // Return results or handle errors
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch popular movies: " + response.errorBody().string());
        }
    }

}