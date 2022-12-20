package com.kenzie.appserver.service;


import com.kenzie.capstone.service.client.LambdaServiceClient;


import java.util.List;

public class RecipeService {

    private LambdaServiceClient lambdaServiceClient;


    public RecipeService(LambdaServiceClient lambdaServiceClient) {
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public String getRecipesByRestrictions(List<String> dietaryRestrictions) {
        //Transform the List of Strings {eggs, bacon, salt, pepper} into a single String "eggs,bacon,salt,pepper"
        StringBuilder sb = new StringBuilder();
        dietaryRestrictions.forEach(dietaryRestriction -> sb.append(dietaryRestriction).append(" "));

        String response = lambdaServiceClient.getRecipesByRestrictions(sb.toString().trim().replace(" ", ","));
        return response;
    }

    public String getRecipesByIngredients(List<String> ingredients) {
        //Transform the List of Strings {eggs, bacon, salt, pepper} into a single String "eggs,bacon,salt,pepper"
        StringBuilder sb = new StringBuilder();
        ingredients.forEach(ingredient -> sb.append(ingredient).append(" "));

        String response = lambdaServiceClient.getRecipesByIngredients(sb.toString().trim().replace(" ", ","));
        return response;
    }
}
