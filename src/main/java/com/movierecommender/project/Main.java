package com.movierecommender.project;

import com.movierecommender.project.dao.IMovieDAO;
import com.movierecommender.project.dao.IMovieRetrofitDAO;
import com.movierecommender.project.dao.RetrofitClientInstance;
import com.movierecommender.project.dto.Movie;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

public class Main implements IMovieDAO {
    public static void main(String[] args) {
        try {

            Retrofit retrofitInstance = RetrofitClientInstance.getRetrofitInstance();
            IMovieRetrofitDAO movieRetrofitDAO = retrofitInstance.create(IMovieRetrofitDAO.class);

            Call<List<Movie>> call = movieRetrofitDAO.searchMovies("Bearer " + API_KEY, "Inception", false, "en-US", 1);
            Response<List<Movie>> response = call.execute();

            if (response.isSuccessful()) {
                System.out.println("Response: " + response.body());
            } else {
                System.out.println("Failed: " + response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmZmYTM2NjgyYTY5ZDM1OWQ1MjQ4OTFkNDQ1OGI2NiIsIm5iZiI6MTcyOTYyNDQ5MS43MjE2MTgsInN1YiI6IjY3MTdmODBjNmU0MjEwNzgwZjc4NzRlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ubnKxHe4mfL51OE8NhOz31gacsvQU0wyh1Ja6vfp4D0";

    @Override
    public List<Movie> fetchMoviesByQuery(String query) throws IOException {
        return List.of();
    }

    @Override
    public List<Movie> fetchPopularMovies() throws IOException {
        return List.of();
    }
}

