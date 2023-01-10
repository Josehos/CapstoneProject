package com.kenzie.appserver.service;

import com.kenzie.appserver.service.model.UserProfile;
import com.kenzie.capstone.service.client.EndpointUtility;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

public class RecipeServiceTest {

    private RecipeService recipeService;

    private LambdaServiceClient lambdaServiceClient;

    private UserProfile userProfile;

    private EndpointUtility endpointUtility;

    @BeforeEach
    void setup() {
        userProfile = mock(UserProfile.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        endpointUtility = mock(EndpointUtility.class);
        recipeService = new RecipeService(lambdaServiceClient,userProfile);
    }

    @Test
    void getRecipesByIngredients() {

        List<String> intolerances = List.of("gluten, dairy, eggs");
        List<String> ingredients = List.of("tomato, cheese, rice");

        StringBuilder dietaryRestrictions = new StringBuilder();
        intolerances.forEach(ingredient -> dietaryRestrictions.append(ingredient).append(" "));

        StringBuilder includedIngredients = new StringBuilder();
        ingredients.forEach(ingredient -> includedIngredients.append(ingredient).append(" "));

        String dr = dietaryRestrictions.toString().trim().replace(" ", ",");
        String includedIn = includedIngredients.toString().trim().replace(" ", ",");

        String response = lambdaServiceClient.getRecipesByIngredients(dr,
                includedIn);

        when(recipeService.getRecipesByIngredients(intolerances,ingredients)).thenReturn(response);


        verify(lambdaServiceClient).getRecipesByIngredients(dr,includedIn);
        Assertions.assertEquals(lambdaServiceClient.getRecipesByIngredients(dr,
                includedIn),response);

    }

    @Test
    void getRecipesById() {
        String recipeId = randomUUID().toString();
        String response = endpointUtility.getEndpoint(recipeId.replace("{id}", recipeId));
        when(recipeService.getRecipeById(recipeId)).thenReturn(response);

        String actual = recipeService.getRecipeById(recipeId);

        verify(lambdaServiceClient).getRecipeById(recipeId);
        Assertions.assertEquals(response,actual);
    }
}
