package com.kenzie.appserver;

import com.kenzie.appserver.controller.model.UserCreateRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtility {

    private final MockMvc mvc;
    private final QueryUtility queryUtility;

    private final String USERNAME = "testUser";
    private final List<String> ALL_FAVORITES = List.of("chili", "pizza", "eggs benedict", "steak", "burgers",
            "spaghetti", "fettuccine alfredo", "beef and broccoli");
    private final List<String> ALL_RESTRICTIONS = List.of("dairy", "egg", "gluten", "grain", "peanut",
            "seafood", "sesame", "shellfish", "soy", "sulfite", "tree nut", "wheat");

    private int counter = 1;

    public TestUtility(MockMvc mvc, QueryUtility queryUtility) {
        this.mvc = mvc;
        this.queryUtility = queryUtility;
    }

    public void createTestDataSet() throws Exception {
        List<UserCreateRequest> requestList = createRequestList();

        for (UserCreateRequest request : requestList) {
            queryUtility.userControllerClient.addNewUser(request);
        }

        System.out.println("Test data created!");
    }

    public void cleanUpTestDataSet() throws Exception {
        for (int i = 1; i < 11; i++) {
            queryUtility.userControllerClient.deleteUser(USERNAME + i);
        }
    }

    public UserCreateRequest createSingleRequest() {

        UserCreateRequest request = new UserCreateRequest();
        request.setUsername(USERNAME + counter);
        request.setFavoriteRecipes(createFavoriteRecipes());
        request.setDietaryRestrictions(createDietaryRestrictions());

        counter++;

        return request;
    }

    private List<UserCreateRequest> createRequestList() {
        List<UserCreateRequest> requestList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            requestList.add(createSingleRequest());
        }

        return requestList;
    }

    private List<String> createFavoriteRecipes() {
        List<String> favorites = new ArrayList<>();
        int[] randomSelections = getRandom(ALL_FAVORITES.size() - 1);
        favorites.add(ALL_FAVORITES.get(randomSelections[0]));
        favorites.add(ALL_FAVORITES.get(randomSelections[1]));
        return favorites;
    }

    private List<String> createDietaryRestrictions() {
        List<String> restrictions = new ArrayList<>();
        int[] randomSelections = getRandom(ALL_RESTRICTIONS.size() - 1);
        restrictions.add(ALL_RESTRICTIONS.get(randomSelections[0]));
        restrictions.add(ALL_RESTRICTIONS.get(randomSelections[1]));
        return restrictions;
    }

    private int[] getRandom(int bound) {
        Random random = new Random();
        int[] randomSelections = new int[2];
        randomSelections[0] = random.nextInt(bound - 1);
        randomSelections[1] = random.nextInt(bound - 1);
        while (randomSelections[0] == randomSelections[1]) {
            randomSelections[1] = random.nextInt(bound - 1);
        }
        return randomSelections;
    }
}
