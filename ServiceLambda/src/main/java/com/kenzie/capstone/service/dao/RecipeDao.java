package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RecipeDao {
    private static final String URL = "https://api.spoonacular.com/recipes";
    private static final String INTOLERANCE = "/complexSearch?number=5&intolerances=";
    private static final String INGREDIENTS = "&includeIngredients=";
    private final String RECIPE_ID = "/{ID}/information?includeNutrition=false";
//    private static final String API_KEY = "&apiKey=ac20e561ee6c4d8db924b44a0b3db6db";
    private static final String API_KEY = "&apiKey=24c9edcac34c4beeab2ec786f687f711";

    public String getRecipesByIngredients(String intolerances, String ingredients) {
        HttpClient client = HttpClient.newHttpClient();

        //Building URL: https://api.spoonacular.com/recipes/complexSearch?intolerances=egg,shellfish&includeIngredients=rice,corn
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL.concat(INTOLERANCE + intolerances).concat(INGREDIENTS + ingredients).concat(API_KEY)))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = httpResponse.statusCode();
            if (statusCode == 200) {
                return httpResponse.body();
            } else {
                return String.format("GET request failed: %d status code received", statusCode);
            }

        } catch (IOException | InterruptedException e) {
            return e.getMessage();
        }
    }

    public String getRecipeById(String recipeId) {
        HttpClient client = HttpClient.newHttpClient();

        //Building URL: https://api.spoonacular.com/recipes/632812/information?includeNutrition=false
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL.concat(RECIPE_ID).replace("{ID}", recipeId).concat(API_KEY)))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = httpResponse.statusCode();

            if (statusCode == 200) {
                return httpResponse.body();
            } else {
                return String.format("GET request failed: %d status code received", statusCode);
            }

        } catch (IOException | InterruptedException e) {
            return e.getMessage();
        }
    }
}
