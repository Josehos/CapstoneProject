package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Objects;


@DynamoDBTable(tableName = "UserProfileTable")
public class UserProfileRecord {

    private String username;

    private List<String> dietaryRestrictions;

    private List<String> favoriteRecipes;


    @DynamoDBHashKey(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute(attributeName = "dietaryRestrictions")
    public List<String> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(List<String> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    @DynamoDBAttribute(attributeName = "favoriteRecipes")
    public List<String> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void setFavoriteRecipes(List<String> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfileRecord)) return false;
        UserProfileRecord that = (UserProfileRecord) o;
        return username.equals(that.username) && Objects.equals(dietaryRestrictions, that.dietaryRestrictions) && Objects.equals(favoriteRecipes, that.favoriteRecipes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, dietaryRestrictions, favoriteRecipes);
    }
}
