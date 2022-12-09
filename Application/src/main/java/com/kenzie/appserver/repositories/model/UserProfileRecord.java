package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Objects;


@DynamoDBTable(tableName = "UserProfileTable")
public class UserProfileRecord {

    private String id;

    private String userName;

    private List<String> dietaryRestrictions;

    private List<String> favoriteRecipes;


    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileRecord that = (UserProfileRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(userName, that.userName)
                && Objects.equals(dietaryRestrictions, that.dietaryRestrictions)
                && Objects.equals(favoriteRecipes, that.favoriteRecipes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, dietaryRestrictions, favoriteRecipes);
    }
}
