package com.kenzie.appserver.service;

import com.kenzie.appserver.UserProfileDao;
import com.kenzie.appserver.exception.UserNotFoundException;
import com.kenzie.appserver.repositories.model.UserProfileRecord;
import com.kenzie.appserver.service.model.UserProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserProfileServiceTest {
    private UserProfileService userProfileService;
    private UserProfileDao userDao;

    private String TEST_USERNAME = "testUsername";
    @BeforeEach
    void setup() {
        userDao = mock(UserProfileDao.class);
        userProfileService = new UserProfileService(userDao);
    }
    /** ------------------------------------------------------------------------
     *  exampleService.findById
     *  ------------------------------------------------------------------------ **/

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
    void getUser_doesntExist_throwsException() {
        // GIVEN
        UserProfileRecord record = createTestUserRecord();

        when(userDao.getUser(record.getUsername())).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(UserNotFoundException.class, () -> userProfileService.getProfile(record.getUsername()));
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
