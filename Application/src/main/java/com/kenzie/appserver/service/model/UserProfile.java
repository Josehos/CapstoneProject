package com.kenzie.appserver.service.model;

import java.util.Collections;
import java.util.List;

public class UserProfile {

    private String id;

    private String userName;

    private List<String> dietaryRestrictions;

    private List<String> favoriteRecipes;

    private List<String> shoppingList;

    private List<String> excludedIngredients;

    private List<String> pantryIngredients;

    public UserProfile(String id, String userName, List<String> dietaryRestrictions, List<String> favoriteRecipes,
                       List<String> shoppingList, List<String> excludedIngredients, List<String> pantryIngredients) {
        if (id == null || id.equals("")) {
            this.id = generateId(userName);
        } else {
            this.id = id;
        }
        this.userName = userName;
        this.dietaryRestrictions = dietaryRestrictions;
        this.favoriteRecipes = favoriteRecipes;
        this.shoppingList = shoppingList;
        this.excludedIngredients = excludedIngredients;
        this.pantryIngredients = pantryIngredients;
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

    public List<String> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<String> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public List<String> getExcludedIngredients() {
        return excludedIngredients;
    }

    public void setExcludedIngredients(List<String> excludedIngredients) {
        this.excludedIngredients = excludedIngredients;
    }

    public List<String> getPantryIngredients() {
        return pantryIngredients;
    }

    public void setPantryIngredients(List<String> pantryIngredients) {
        this.pantryIngredients = pantryIngredients;
    }

    public static String generateId(String userName) {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append((int) (Math.random() * 10));
        }
        return userName.substring(0, 4) + id;
    }
}

