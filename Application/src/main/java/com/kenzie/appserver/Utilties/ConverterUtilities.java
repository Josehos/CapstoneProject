package com.kenzie.appserver.Utilties;

import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.model.UserProfileRecord;
import com.kenzie.appserver.service.model.UserProfile;

public class ConverterUtilities {

    public static UserProfileRecord createRecordFromProfile(UserProfile userProfile) {
        UserProfileRecord record = new UserProfileRecord();
        record.setUsername(userProfile.getUsername());
        record.setDietaryRestrictions(userProfile.getDietaryRestrictions());
        record.setFavoriteRecipes(userProfile.getFavoriteRecipes());
        return record;
    }

    public static UserCreateRequest createRequestFromProfile (UserProfile user) {
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername(user.getUsername());
        request.setFavoriteRecipes(user.getFavoriteRecipes());
        request.setDietaryRestrictions(user.getDietaryRestrictions());
        return request;
    }

    public static UserProfile createProfileFromRequest(UserCreateRequest request) {
        UserProfile user = new UserProfile(request.getUsername(),
                request.getDietaryRestrictions(),
                request.getFavoriteRecipes());

        return user;
    }

    public static UserResponse createResponseFromProfile(UserProfile profile) {
        UserResponse response = new UserResponse();
        response.setUsername(profile.getUsername());
        response.setDietaryRestrictions(profile.getDietaryRestrictions());
        response.setFavoriteRecipes(profile.getFavoriteRecipes());
        return response;
    }

    public static UserProfile createProfileFromRecord(UserProfileRecord record) {
        UserProfile user = new UserProfile();
        user.setUsername(record.getUsername());
        user.setFavoriteRecipes(record.getFavoriteRecipes());
        user.setDietaryRestrictions(record.getDietaryRestrictions());
        return user;
    }
}
