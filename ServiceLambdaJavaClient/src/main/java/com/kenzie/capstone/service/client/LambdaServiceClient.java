package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;


public class LambdaServiceClient {

    private final String INGREDIENTS = "/intolerances/{intolerances}/ingredients/{includeIngredients}";
    private final String RECIPE_ID = "/recipes/{id}";


    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public String getRecipesByIngredients(String intolerances, String ingredients) {
        EndpointUtility endpointUtility = new EndpointUtility();

        String response = endpointUtility.getEndpoint(INGREDIENTS.replace("{intolerances}", intolerances)
                .replace("{includeIngredients}", ingredients));

        return response;
    }

    public String getRecipeById(String recipeId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(RECIPE_ID.replace("{id}", recipeId));

        return response;
    }
}
