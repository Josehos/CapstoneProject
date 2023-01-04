package com.kenzie.appserver.service;

import com.kenzie.appserver.UserProfileDao;
import com.kenzie.appserver.repositories.model.UserProfileRecord;
import com.kenzie.appserver.service.model.UserProfile;

public class UserProfileService {

    private UserProfileDao userProfileDao;

    public UserProfileService(UserProfileDao userProfileDao){
        this.userProfileDao = userProfileDao;
    }

    public UserProfile getProfile(String username) {
        UserProfileRecord record = userProfileDao.getUser(username);
        return new UserProfile(record.getUsername(),record.getDietaryRestrictions(),
                record.getFavoriteRecipes());
    }

    public UserProfile addProfile(UserProfile userProfile) {
        if(userProfileDao.getUser(userProfile.getUsername()) != null) {
            throw new RuntimeException("User already exists");
        }
        userProfileDao.addUser(userProfile);
        return userProfile;
    }

    public UserProfile updateProfile(UserProfile userProfile) {
        if(userProfileDao.getUser(userProfile.getUsername()) != null) {
            userProfileDao.deleteUser(userProfile.getUsername());
            userProfileDao.addUser(userProfile);
        }
        else{
            throw new RuntimeException("User does not exist");
        }
        return userProfile;
    }

    public void deleteUserProfile (String username) {
        if(userProfileDao.getUser(username) != null) {
            userProfileDao.deleteUser(username);
        }
        else{
            throw new RuntimeException("User does not exist");
        }
    }


}
