package com.kenzie.appserver.service;

import com.kenzie.appserver.service.model.UserProfile;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.ExampleData;

import java.util.List;

public class RecipeService {

    private LambdaServiceClient lambdaServiceClient;
    private UserProfile userProfile;

    /*
    Add cache to this service
     */

    public RecipeService(LambdaServiceClient lambdaServiceClient, UserProfile userProfile) {
        this.lambdaServiceClient = lambdaServiceClient;
        this.userProfile = userProfile;
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

    /*
    Pulling the dietaryRestrictions from the UserProfile compared to having the dietaryRestrictions as an
    input parameter.
     */
    public String getRecipesByRestrictions() {
        //Transform the List of Strings {eggs, bacon, salt, pepper} into a single String "eggs,bacon,salt,pepper"
        StringBuilder sb = new StringBuilder();
        this.userProfile.getDietaryRestrictions().forEach(dietaryRestriction -> sb.append(dietaryRestriction).append(" "));

        String response = lambdaServiceClient.getRecipesByRestrictions(sb.toString().trim().replace(" ", ","));
        return response;
    }
}
