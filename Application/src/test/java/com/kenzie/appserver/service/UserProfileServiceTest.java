package com.kenzie.appserver.service;

import com.kenzie.appserver.dao.UserProfileDao;
import com.kenzie.appserver.repositories.model.UserProfileRecord;
import com.kenzie.appserver.service.model.UserProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class UserProfileServiceTest {
    private UserProfileService userProfileService;
    private UserProfileDao userDao;

    private String TEST_USERNAME = "testUsername";
    @BeforeEach
    void setup() {
        userDao = mock(UserProfileDao.class);
        userProfileService = new UserProfileService(userDao);
    }

    @Test
    void getProfile() {
        // GIVEN
        UserProfileRecord record = createTestUserRecord();

        // WHEN
        when(userDao.getUser(record.getUsername())).thenReturn(Optional.of(record));
        UserProfile user = userProfileService.getProfile(record.getUsername());

        // THEN
        Assertions.assertNotNull(user, "The user is returned");
        Assertions.assertEquals(record.getUsername(), user.getUsername(), "The username matches");
        Assertions.assertEquals(record.getDietaryRestrictions(), user.getDietaryRestrictions(), "The dietary restrictions match");
    }

    @Test
    void getUser_doesntExist_returnsNull() {
        // GIVEN
        UserProfileRecord record = createTestUserRecord();

        when(userDao.getUser(record.getUsername())).thenReturn(Optional.empty());

        // WHEN & THEN
        assertNull(userProfileService.getProfile(record.getUsername()));
    }

    @Test
    void addProfile() {
        //GIVEN
        UserProfile  userProfile = new UserProfile(TEST_USERNAME, Collections.emptyList(),
                Collections.emptyList());
        UserProfileRecord record = createTestUserRecord();

        ArgumentCaptor<UserProfile> userProfileArgumentCaptor = ArgumentCaptor.forClass(UserProfile.class);

        when(userDao.getUser(record.getUsername())).thenReturn(Optional.empty());
        UserProfile returnedProfile = userProfileService.addProfile(userProfile);


        verify(userDao).addUser(userProfileArgumentCaptor.capture());

        UserProfile profile = userProfileArgumentCaptor.getValue();

        Assertions.assertNotNull(returnedProfile);
        Assertions.assertNotNull(profile, "The userprofile has returned");
        Assertions.assertEquals(profile,returnedProfile);

    }

    @Test
    void updateProfile() {
        UserProfile  userProfile = new UserProfile(TEST_USERNAME, Collections.emptyList(),
                Collections.emptyList());

        UserProfileRecord record = createTestUserRecord();

        List<String> favoriteRecipes = new ArrayList<>();
        favoriteRecipes.add("spaghetti");
        favoriteRecipes.add("hamburger");

        userProfile.setFavoriteRecipes(favoriteRecipes);

        when(userDao.getUser(record.getUsername())).thenReturn(Optional.of(record));

        ArgumentCaptor<UserProfile> userProfileArgumentCaptor = ArgumentCaptor.forClass(UserProfile.class);

        UserProfile returnedProfile = userProfileService.updateProfile(userProfile);

        verify(userDao).addUser(userProfileArgumentCaptor.capture());

        Assertions.assertNotNull(returnedProfile);
        Assertions.assertEquals(userProfile.getUsername(),returnedProfile.getUsername());
        Assertions.assertEquals(userProfile.getDietaryRestrictions(),returnedProfile.getDietaryRestrictions());
        Assertions.assertEquals(userProfile.getFavoriteRecipes(),returnedProfile.getFavoriteRecipes());

    }

    @Test
    void deleteUserProfile() {
        UserProfileRecord record = createTestUserRecord();

        when(userDao.getUser(record.getUsername())).thenReturn(Optional.of(record));

        userProfileService.deleteUserProfile(record.getUsername());

        verify(userDao).deleteUser(record.getUsername());
    }

    @Test
    void addProfile_profileAlreadyExist_returnsNull() {
        UserProfile  userProfile = new UserProfile(TEST_USERNAME, Collections.emptyList(),
                Collections.emptyList());
        UserProfileRecord record = createTestUserRecord();

        when(userDao.getUser(record.getUsername())).thenReturn(Optional.of(record));

        assertNull(userProfileService.addProfile(userProfile));
    }

    @Test
    void updateProfile_profileNotFound_returnsNull() {
        UserProfile  userProfile = new UserProfile(TEST_USERNAME, Collections.emptyList(),
                Collections.emptyList());
        UserProfileRecord record = createTestUserRecord();

        when(userDao.getUser(record.getUsername())).thenReturn(Optional.empty());

        assertNull(userProfileService.updateProfile(userProfile));
    }

    @Test
    void deleteUserProfile_profileNotFound_returnsNull() {
        UserProfileRecord record = createTestUserRecord();

        when(userDao.getUser(record.getUsername())).thenReturn(Optional.empty());

        assertNull(userProfileService.deleteUserProfile(record.getUsername()));
    }

    private UserProfileRecord createTestUserRecord() {
        String username = TEST_USERNAME;
        List<String> dietaryRestrictions = new ArrayList<>();
        dietaryRestrictions.add("Dairy");
        dietaryRestrictions.add("Eggs");

        UserProfileRecord record = new UserProfileRecord();
        record.setUsername(username);
        record.setDietaryRestrictions(dietaryRestrictions);

        return record;
    }

}
