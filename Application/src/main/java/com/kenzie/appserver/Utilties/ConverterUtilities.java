package com.kenzie.appserver.Utilties;

import com.kenzie.appserver.repositories.model.UserProfileRecord;
import com.kenzie.appserver.service.model.UserProfile;

public class ConverterUtilities {

    public static UserProfileRecord createRecordFromUserProfile(UserProfile userProfile) {
        UserProfileRecord record = new UserProfileRecord();
        record.setId(userProfile.getId());
        record.setUsername(userProfile.getUsername());
        record.setDietaryRestrictions(userProfile.getDietaryRestrictions());
        record.setFavoriteRecipes(userProfile.getFavoriteRecipes());
        return record;
    }
}
