package com.kenzie.appserver.service.model;

import java.util.Collections;
import java.util.List;

public class UserProfile {

    private String id;

    private String userName;

    private List<String> dietaryRestrictions;

    private List<String> favoriteRecipes;



    public UserProfile(String id, String userName, List<String> dietaryRestrictions,
                       List<String> favoriteRecipes) {
        if (id == null || id.equals("")) {
            this.id = generateId(userName);
        } else {
            this.id = id;
        }
        this.userName = userName;
        this.dietaryRestrictions = dietaryRestrictions;
        this.favoriteRecipes = favoriteRecipes;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(List<String> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public List<String> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void setFavoriteRecipes(List<String> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    public static String generateId(String userName) {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append((int) (Math.random() * 10));
        }
        return userName.substring(0, 4) + id;
    }
}

