package com.movierecommender.project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProfileControllerTest
{
    @Autowired
    private ProfileController controller; //Handles requests related to the profile page

    @MockBean
    private Model model; //Used to pass attributes between the controller and the view

    @Test
    public void testShowProfile()
    {
        //If "favoriteGenres" returns null when requested
        when(model.getAttribute("favoriteGenres")).thenReturn(null);

        //Storing the view name
        String view = controller.showProfile(model);

        //Assert that the returned view name is "profile"
        assertEquals("profile", view);

        //Verify that the new list is added to the model
        verify(model).addAttribute("favoriteGenres", new ArrayList<>());
    }

    @Test
    public void testAddFavoriteGenre()
    {
        //Create a list of favorite genres and add "Action" as the initial genre
        List<String> favoriteGenres = new ArrayList<>();
        favoriteGenres.add("Action");

        //Return the favoriteGenres list when "favoriteGenres" is requested
        when(model.getAttribute("favoriteGenres")).thenReturn(favoriteGenres);

        //Selecting a genre to add to the list
        String selectedGenre = "Comedy";

        //Adding the genre to the list
        controller.addFavoriteGenre(selectedGenre, model);

        //Verify that the addAttribute method was called with the updated favoriteGenres list
        verify(model).addAttribute("favoriteGenres", favoriteGenres);

        //Assert the size of the list is 2
        assertEquals(2, favoriteGenres.size());

        //Assert that "Comedy" was added to the list
        assertTrue(favoriteGenres.contains("Comedy"));
    }
}