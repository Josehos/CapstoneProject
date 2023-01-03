package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;


public class LambdaServiceClient {

    private static final String GET_EXAMPLE_ENDPOINT = "example/{id}";
    private static final String SET_EXAMPLE_ENDPOINT = "example";


//    private final String DIETARY_RESTRICTIONS = "?intolerances=";
//    private final String INGREDIENTS = "?includeIngredients=";

    private final String DIETARY_RESTRICTIONS = "/intolerance/{intolerances}";
    private final String INGREDIENTS = "/ingredients/{includeIngredients}";


    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

//    public ExampleData getExampleData(String id) {
//        EndpointUtility endpointUtility = new EndpointUtility();
//        String response = endpointUtility.getEndpoint(GET_EXAMPLE_ENDPOINT.replace("{id}", id));
//        ExampleData exampleData;
//        try {
//            exampleData = mapper.readValue(response, ExampleData.class);
//        } catch (Exception e) {
//            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
//        }
//        return exampleData;
//    }
//
//    public ExampleData setExampleData(String data) {
//        EndpointUtility endpointUtility = new EndpointUtility();
//        String response = endpointUtility.postEndpoint(SET_EXAMPLE_ENDPOINT, data);
//        ExampleData exampleData;
//        try {
//            exampleData = mapper.readValue(response, ExampleData.class);
//        } catch (Exception e) {
//            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
//        }
//        return exampleData;
//    }


    public String getRecipesByRestrictions(String intolerance) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(DIETARY_RESTRICTIONS.replace("{intolerance}", intolerance));

        return response;
    }

    public String getRecipesByIngredients(String ingredients) {
        EndpointUtility endpointUtility = new EndpointUtility();

        String response = endpointUtility.getEndpoint(INGREDIENTS.replace("{includeIngredients}", ingredients));
        return response;
    }
}
