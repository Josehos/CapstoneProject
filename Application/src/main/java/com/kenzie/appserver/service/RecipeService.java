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

    public String getRecipesByIngredients(List<String> ingredients) {
        //Pulling intolerance data from the UserProfile and transforming it into a String
        StringBuilder intolerances = new StringBuilder();
        userProfile.getDietaryRestrictions().forEach(restriction -> intolerances.append(restriction).append(" "));

        StringBuilder includedIngredients = new StringBuilder();
        ingredients.forEach(ingredient -> includedIngredients.append(ingredient).append(" "));

        String response = lambdaServiceClient.getRecipesByIngredients(intolerances.toString().trim().replace(" ", ","),
                includedIngredients.toString().trim().replace(" ", ","));

        return response;
    }

    public String getRecipeById(String recipeId) {
        String response = lambdaServiceClient.getRecipeById(recipeId);

        return response;
    }
}
