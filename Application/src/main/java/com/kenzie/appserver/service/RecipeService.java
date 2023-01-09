package com.kenzie.appserver.service;


import com.kenzie.appserver.service.model.UserProfile;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.stereotype.Service;


import java.nio.file.attribute.UserPrincipal;
import java.util.List;

@Service
public class RecipeService {

    private LambdaServiceClient lambdaServiceClient;
    private UserProfile userProfile;


    public RecipeService(LambdaServiceClient lambdaServiceClient, UserProfile userProfile) {
        this.lambdaServiceClient = lambdaServiceClient;
        this.userProfile = userProfile;
    }

    public String getRecipesByIngredients(List<String> intolerances, List<String> ingredients) {
        //Transform the List of intolerances {gluten, dairy, eggs} into a single String "gluten,dairy,eggs"
        StringBuilder dietaryRestrictions = new StringBuilder();
        intolerances.forEach(ingredient -> dietaryRestrictions.append(ingredient).append(" "));

        //Transform the List of ingredients {tomato, cheese, rice} into a single String "tomato,cheese,rice"
        StringBuilder includedIngredients = new StringBuilder();
        ingredients.forEach(ingredient -> includedIngredients.append(ingredient).append(" "));

        String response = lambdaServiceClient.getRecipesByIngredients(dietaryRestrictions.toString().trim().replace(" ", ","),
                includedIngredients.toString().trim().replace(" ", ","));

        return response;
    }

    public String getRecipeById(String recipeId) {
        String response = lambdaServiceClient.getRecipeById(recipeId);

        return response;
    }
}
