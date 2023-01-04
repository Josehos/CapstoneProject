package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.kenzie.appserver.UserProfileDao;
import com.kenzie.appserver.repositories.model.UserProfileRecord;
import com.kenzie.appserver.service.model.UserProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

public class UserProfileServiceTest {

    private UserProfileService userProfileService;

    private UserProfileDao userProfileDao;


    @BeforeEach
    void setup() {
        userProfileDao = mock(UserProfileDao.class);
        userProfileService = new UserProfileService(userProfileDao);
    }

    @Test
    void addProfile() {
        //GIVEN
        UserProfile  userProfile = new UserProfile("","jakeT", Collections.emptyList(),
                Collections.emptyList());

        ArgumentCaptor<UserProfile> userProfileArgumentCaptor = ArgumentCaptor.forClass(UserProfile.class);

        UserProfile returnedProfile = userProfileService.addProfile(userProfile);


        verify(userProfileDao).addUser(userProfileArgumentCaptor.capture());

        UserProfile profile = userProfileArgumentCaptor.getValue();

        Assertions.assertNotNull(returnedProfile);
        Assertions.assertNotNull(profile, "The userprofile has returned");
        Assertions.assertEquals(profile,returnedProfile);

    }

    @Test
    void getProfile() {
        String userProfileId = randomUUID().toString();

        UserProfile  userProfile = new UserProfile(userProfileId,"jakeT", Collections.emptyList(),
                Collections.emptyList());

        UserProfileRecord record = new UserProfileRecord();
        record.setId(userProfileId);
        record.setFavoriteRecipes(userProfile.getFavoriteRecipes());
        record.setUsername(userProfile.getUsername());
        record.setDietaryRestrictions(userProfile.getDietaryRestrictions());

        when(userProfileDao.getUser(userProfileId)).thenReturn(record);

        UserProfile returnedProfile = userProfileService.getProfile(userProfileId);

        verify(userProfileDao).getUser(userProfileId);

        UserProfile profile = new UserProfile(record.getId(),record.getUsername(),
                record.getDietaryRestrictions(),record.getFavoriteRecipes());

        Assertions.assertNotNull(returnedProfile);
        Assertions.assertNotNull(profile);
        Assertions.assertEquals(profile.getUsername(),returnedProfile.getUsername());
        Assertions.assertEquals(profile.getId(),returnedProfile.getId());
        Assertions.assertEquals(profile.getDietaryRestrictions(),returnedProfile.getDietaryRestrictions());
        Assertions.assertEquals(profile.getFavoriteRecipes(),returnedProfile.getFavoriteRecipes());

    }

    @Test
    void updateProfile() {
        String userProfileId = randomUUID().toString();

        UserProfile  userProfile = new UserProfile(userProfileId,"jakeT", Collections.emptyList(),
                Collections.emptyList());

        UserProfileRecord record = new UserProfileRecord();
        record.setId(userProfileId);
        record.setFavoriteRecipes(userProfile.getFavoriteRecipes());
        record.setUsername(userProfile.getUsername());
        record.setDietaryRestrictions(userProfile.getDietaryRestrictions());

        List<String> favoriteRecipes = new ArrayList<>();
        favoriteRecipes.add("spaghetti");
        favoriteRecipes.add("hamburger");

        userProfile.setFavoriteRecipes(favoriteRecipes);

        when(userProfileDao.getUser(userProfileId)).thenReturn(record);

        ArgumentCaptor<UserProfile> userProfileArgumentCaptor = ArgumentCaptor.forClass(UserProfile.class);

        UserProfile returnedProfile = userProfileService.updateProfile(userProfile);

        verify(userProfileDao).addUser(userProfileArgumentCaptor.capture());

        Assertions.assertNotNull(returnedProfile);
        Assertions.assertEquals(userProfile.getId(),returnedProfile.getId());
        Assertions.assertEquals(userProfile.getUsername(),returnedProfile.getUsername());
        Assertions.assertEquals(userProfile.getDietaryRestrictions(),returnedProfile.getDietaryRestrictions());
        Assertions.assertEquals(userProfile.getFavoriteRecipes(),returnedProfile.getFavoriteRecipes());

    }

    @Test
    void deleteUser() {
        String userProfileId = randomUUID().toString();

        UserProfile  userProfile = new UserProfile(userProfileId,"jakeT", Collections.emptyList(),
                Collections.emptyList());

        UserProfileRecord record = new UserProfileRecord();
        record.setId(userProfileId);
        record.setFavoriteRecipes(userProfile.getFavoriteRecipes());
        record.setUsername(userProfile.getUsername());
        record.setDietaryRestrictions(userProfile.getDietaryRestrictions());

        when(userProfileDao.getUser(userProfileId)).thenReturn(record);

        userProfileService.deleteUserProfile(userProfileId);

        verify(userProfileDao).deleteUser(userProfileId);

    }

}
