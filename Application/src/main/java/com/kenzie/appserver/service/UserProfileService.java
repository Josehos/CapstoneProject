package com.kenzie.appserver.service;

import com.kenzie.appserver.UserProfileDao;
import com.kenzie.appserver.repositories.model.UserProfileRecord;
import com.kenzie.appserver.service.model.UserProfile;

import static com.kenzie.appserver.Utilties.ConverterUtilities.createRecordFromUserProfile;

public class UserProfileService {

    private UserProfileDao userProfileDao;

    public UserProfileService(UserProfileDao userProfileDao){
        this.userProfileDao = userProfileDao;
    }

    public UserProfile getProfile (String id) {
        UserProfileRecord record = userProfileDao.getUser(id);
        return new UserProfile(record.getId(),record.getUsername(),record.getDietaryRestrictions(),
                record.getFavoriteRecipes());
    }

    public UserProfile addProfile (UserProfile userProfile) {
        if(userProfileDao.getUser(userProfile.getId()) != null) {
            throw new RuntimeException("User already exist");
        }
        userProfileDao.addUser(userProfile);
        return userProfile;
    }

    public UserProfile updateProfile(UserProfile userProfile) {
        if(userProfileDao.getUser(userProfile.getId()) != null) {
            userProfileDao.deleteUser(userProfile.getId());
            userProfileDao.addUser(userProfile);
        }
        else{
            throw new RuntimeException("User does not exist");
        }
        return userProfile;
    }

    public void deleteUserProfile (String id) {
        if(userProfileDao.getUser(id) != null) {
            userProfileDao.deleteUser(id);
        }
        else{
            throw new RuntimeException("User does not exist");
        }
    }


}
