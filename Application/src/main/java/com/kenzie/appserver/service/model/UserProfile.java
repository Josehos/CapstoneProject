package com.kenzie.appserver.service.model;

import java.util.Collections;
import java.util.List;

public class UserProfile {

    private String id;

    private String username;

    private List<String> dietaryRestrictions;

    private List<String> favoriteRecipes;



    public UserProfile(String id, String username, List<String> dietaryRestrictions,
                       List<String> favoriteRecipes) {
        if (id == null || id.equals("")) {
            this.id = generateId(username);
        } else {
            this.id = id;
        }
        this.username = username;
        this.dietaryRestrictions = dietaryRestrictions;
        this.favoriteRecipes = favoriteRecipes;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
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

