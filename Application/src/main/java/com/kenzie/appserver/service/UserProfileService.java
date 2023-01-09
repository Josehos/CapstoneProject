package com.kenzie.appserver.service;

import com.kenzie.appserver.dao.UserProfileDao;
import com.kenzie.appserver.repositories.model.UserProfileRecord;
import com.kenzie.appserver.service.model.UserProfile;
import org.springframework.stereotype.Service;

import static com.kenzie.appserver.Utilties.ConverterUtilities.createProfileFromRecord;

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
            return null;
        }
        return new UserProfile(record.getUsername(),record.getDietaryRestrictions(),
                record.getFavoriteRecipes());
    }

    public UserProfile addProfile(UserProfile userProfile) {
        if(userProfileDao.getUser(userProfile.getUsername()).isPresent()) {
            System.out.println("A user already exists by that username.");
            return null;
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
            return null;
        }
        return userProfile;
    }

    public UserProfile deleteUserProfile (String username) {
        if(userProfileDao.getUser(username).isPresent()) {
            UserProfile userToDelete = createProfileFromRecord(userProfileDao.getUser(username).get());
            userProfileDao.deleteUser(username);
            return userToDelete;
        }
        else{
            return null;
        }
    }
}
