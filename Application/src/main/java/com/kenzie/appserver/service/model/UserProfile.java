package com.kenzie.appserver.service.model;

import java.util.List;

public class UserProfile {

    private String username;

    private List<String> dietaryRestrictions;

    private List<String> favoriteRecipes;



    public UserProfile(String username, List<String> dietaryRestrictions,
                       List<String> favoriteRecipes) {
        this.username = username;
        this.dietaryRestrictions = dietaryRestrictions;
        this.favoriteRecipes = favoriteRecipes;

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

}

