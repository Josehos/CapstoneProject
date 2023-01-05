package com.kenzie.appserver.service;

import com.kenzie.appserver.dao.UserProfileDao;
import com.kenzie.appserver.exception.UserNotFoundException;
import com.kenzie.appserver.repositories.model.UserProfileRecord;
import com.kenzie.appserver.service.model.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private UserProfileDao userProfileDao;

    public UserProfileService(UserProfileDao userProfileDao){
        this.userProfileDao = userProfileDao;
    }

    public UserProfile getProfile(String username) {
        UserProfileRecord record;
        if (userProfileDao.getUser(username).isPresent()) {
            record = userProfileDao.getUser(username).get();
        } else {
            throw new UserNotFoundException();
        }
        return new UserProfile(record.getUsername(),record.getDietaryRestrictions(),
                record.getFavoriteRecipes());
    }

    public UserProfile addProfile(UserProfile userProfile) {
        if(userProfileDao.getUser(userProfile.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        userProfileDao.addUser(userProfile);
        return userProfile;
    }

    public UserProfile updateProfile(UserProfile userProfile) {
        if(userProfileDao.getUser(userProfile.getUsername()).isPresent()) {
            userProfileDao.deleteUser(userProfile.getUsername());
            userProfileDao.addUser(userProfile);
        }
        else{
            throw new RuntimeException("User does not exist");
        }
        return userProfile;
    }

    public void deleteUserProfile (String username) {
        if(userProfileDao.getUser(username).isPresent()) {
            userProfileDao.deleteUser(username);
        }
        else{
            throw new RuntimeException("User does not exist");
        }
    }


}
