package com.movierecommender.project.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.movierecommender.project.dto.Movie;
import org.springframework.stereotype.Repository;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Class that handles fetching movie data from the Movie Database
 * API using Retrofit.
 * This class contains methods to fetch movies by search query, fetch popular movies,
 * and parse the movie data.
 *
 * @author Marko Nisiama
 */
@Repository
public class MovieDAO implements IMovieDAO {

    private static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NmZmYTM2NjgyYTY5ZDM1OWQ1MjQ4OTFkNDQ1OGI2NiIsIm5iZiI6MTcyOTYyNDQ5MS43MjE2MTgsInN1YiI6IjY3MTdmODBjNmU0MjEwNzgwZjc4NzRlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ubnKxHe4mfL51OE8NhOz31gacsvQU0wyh1Ja6vfp4D0";
    private static final Map<Integer, String> GENRE_MAP = Map.of(
            28, "Action", 35, "Comedy", 18, "Drama", 27, "Horror",
            10749, "Romance", 16, "Animation", 878, "Science Fiction",
            10751, "Family", 12, "Adventure", 53, "Thriller"
    );

    /**
     * Fetches a list of movies based on the provided search query from the API.
     *
     * @param query The search query string to search for movies.
     * @return A list of movie objects matching the search query.
     * @throws IOException If an error occurs during the API request or response parsing.
     */
    @Override
    public List<Movie> fetchMoviesByQuery(String query) throws IOException {
        Retrofit retrofitInstance = RetrofitClientInstance.getRetrofitInstance();
        IMovieRetrofitDAO movieRetrofitDAO = retrofitInstance.create(IMovieRetrofitDAO.class);

        Call<JsonObject> call = movieRetrofitDAO.searchMovies("Bearer " + API_KEY, query, false, "en-US", 1);
        Response<JsonObject> response = call.execute();

        if (response.isSuccessful() && response.body() != null) {
            JsonArray resultsArray = response.body().getAsJsonArray("results");
            return parseMoviesFromJson(resultsArray);
        } else {
            throw new IOException("Failed to fetch movies: " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
        }
    }

    /**
     * Fetches a list of popular movies from the API.
     *
     * @return A list of popular movie objects
     * @throws IOException If an error occurs during the API request or response parsing.
     */
    @Override
    public List<Movie> fetchPopularMovies() throws IOException {
        Retrofit retrofitInstance = RetrofitClientInstance.getRetrofitInstance();
        IMovieRetrofitDAO movieRetrofitDAO = retrofitInstance.create(IMovieRetrofitDAO.class);

        Call<JsonObject> call = movieRetrofitDAO.discoverMovies("Bearer " + API_KEY, false, false, "en-US", 1, "popularity.desc");
        Response<JsonObject> response = call.execute();

        if (response.isSuccessful() && response.body() != null) {
            JsonArray resultsArray = response.body().getAsJsonArray("results");
            return parseMoviesFromJson(resultsArray);
        } else {
            throw new IOException("Failed to fetch popular movies: " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
        }
    }

    /**
     * Parses a JSON array of movie data from the API response into a list of movie objects.
     *
     * @param resultsArray The JSON array containing the movie data.
     * @return A list of movie objects parsed from the JSON data.
     */
    @Override
    public List<Movie> parseMoviesFromJson(JsonArray resultsArray) {
        List<Movie> movies = new ArrayList<>();
        for (JsonElement element : resultsArray) {
            JsonObject movieObject = element.getAsJsonObject();
            Movie movie = new Movie();

            movie.setMovieId(movieObject.get("id").getAsInt());
            movie.setTitle(movieObject.get("original_title").getAsString());
            movie.setDescription(movieObject.get("overview").getAsString());

            String posterPath = movieObject.has("poster_path") && !movieObject.get("poster_path").isJsonNull()
                    ? movieObject.get("poster_path").getAsString()
                    : "/default-poster.jpg";
            movie.setPosterUrl("https://image.tmdb.org/t/p/original" + posterPath);

            movie.setReleaseYear(movieObject.get("release_date").getAsString());
            movie.setRating((int) Math.round(movieObject.get("vote_average").getAsDouble()));

            // Handle genre mapping
            JsonArray genreIds = movieObject.getAsJsonArray("genre_ids");
            String genres = mapGenres(genreIds);
            movie.setGenre(genres);

            movies.add(movie);
        }
        return movies;
    }

    /**
     * Maps the list of genre IDs to their corresponding genre names.
     *
     * @param genreIds The JSON array containing genre IDs.
     * @return A string of comma-separated genre names corresponding to the genre IDs.
     */
    @Override
    public String mapGenres(JsonArray genreIds) {
        List<String> genres = new ArrayList<>();
        for (JsonElement genreIdElement : genreIds) {
            int genreId = genreIdElement.getAsInt();
            String genreName = GENRE_MAP.getOrDefault(genreId, "Unknown");
            genres.add(genreName);
        }
        return String.join(", ", genres);
    }
}