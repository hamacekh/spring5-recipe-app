package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class IndexControllerTest {

    IndexController indexController;

    @Mock
    private RecipeService recipeService;

    @Mock
    private Model model;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService);
    }

    @Test
    public void getIndexPage() {
        Recipe recipe = new Recipe();
        recipe.setId(55L);
        HashSet<Recipe> hashSet = new HashSet<>();
        hashSet.add(recipe);

        when(recipeService.getRecipes()).thenReturn(hashSet);

        String page = indexController.getIndexPage(model);

        assertEquals("index", page);
        verify(model, times(1)).addAttribute("recipes", hashSet);
        verify(recipeService, times(1)).getRecipes();

    }
}