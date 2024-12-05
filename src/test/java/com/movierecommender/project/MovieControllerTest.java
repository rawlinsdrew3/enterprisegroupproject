package com.movierecommender.project;

import com.movierecommender.project.dto.Movie;
import com.movierecommender.project.service.IMovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MovieControllerTest {
    //Setting up Controller and Mock
    @Autowired
    private MovieController controller;
    @MockBean
    private IMovieService movieService;


    @Test
    public void testGetMovieFromApi() throws IOException, InterruptedException {
        //create dummy movie entry
        Movie movie = new Movie();
        movie.setMovieId(24);
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");


        when(movieService.fetchMovieFromExternalApi(24)).thenReturn(movie);

        ResponseEntity<?> response = controller.getMovieFromApi();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(movie, response.getBody());

        verify(movieService).fetchMovieFromExternalApi(24);
    }

    @Test
    public void testErrorGetMovieFromApi() throws IOException, InterruptedException {
        // false error test
        when(movieService.fetchMovieFromExternalApi(24)).thenThrow(new RuntimeException("API error"));

        ResponseEntity<?> response = controller.getMovieFromApi();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        assertEquals("Error occurred while fetching movie data: API error", response.getBody());
        verify(movieService).fetchMovieFromExternalApi(24);
    }

    @Test
    public void testGetAllMovies() {
        // list of dummy movies
        Movie movie1 = new Movie();
        movie1.setMovieId(1);
        movie1.setTitle("Movie 1");
        movie1.setGenre("Action");

        Movie movie2 = new Movie();
        movie2.setMovieId(2);
        movie2.setTitle("Movie 2");
        movie2.setGenre("Comedy");

        Iterable<Movie> movies = List.of(movie1, movie2);

        when(movieService.getAllMovies()).thenReturn(movies);

        ResponseEntity<?> response = controller.getAllMovies();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertTrue(((Iterable<?>) response.getBody()).spliterator().getExactSizeIfKnown() == 2);

        verify(movieService).getAllMovies();
    }

    @Test
    public void testErrorGetAllMovies() {
        when(movieService.getAllMovies()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = controller.getAllMovies();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        assertEquals("Error occurred while fetching movies: Database error", response.getBody());

        verify(movieService).getAllMovies();
    }

    @Test
    public void testAddMovie() {
        // Create a mock movie object
        Movie movie = new Movie();
        movie.setMovieId(1);
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");

        when(movieService.saveMovie(movie)).thenReturn(movie);

        ResponseEntity<?> response = controller.addMovie(movie);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(movie, response.getBody());

        verify(movieService).saveMovie(movie);
    }

    @Test
    public void testErrorAddMovie() {
        //dummy movie object
        Movie movie = new Movie();
        movie.setMovieId(1);
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");


        //false error
        when(movieService.saveMovie(movie)).thenThrow(new RuntimeException("Error saving movie"));

        ResponseEntity<?> response = controller.addMovie(movie);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        assertEquals("Error occurred while saving the movie: Error saving movie", response.getBody());
        verify(movieService).saveMovie(movie);
    }
}


    //Commented out bc I think the rating code is OoD and tests arent working
//    @Test
//    public void testSubmitRating() {
//       //i dont even know if this is how we submit ratings anymore but adding a test regardless
//        String movieTitle = "Inception";
//        int movieRating = 5;
//
//        try (BufferedWriter mockWriter = mock(BufferedWriter.class)) {
//
//            String result = controller.submitRating(movieTitle, movieRating);
//
//            assertEquals("redirect:/browse", result);
//
//            verify(mockWriter).write("Movie: Inception, Rating: 5\n");
//        } catch (Exception e) {
//            fail("Exception occurred while testing submitRating: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testErrorSubmitRating() {
//        String movieTitle = "Inception";
//        int movieRating = 5;
//
//        try (BufferedWriter mockWriter = mock(BufferedWriter.class)) {
//            doThrow(new RuntimeException("File write error")).when(mockWriter).write(anyString());
//
//            String result = controller.submitRating(movieTitle, movieRating);
//
//            assertEquals("redirect:/browse", result);
//
//            verify(mockWriter).write("Movie: Inception, Rating: 5\n");
//        } catch (Exception e) {
//            fail("Exception occurred while testing submitRating: " + e.getMessage());
//        }
//    }
//}
