package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;

import com.kenzie.appserver.service.UserProfileService;
import com.kenzie.appserver.service.model.Example;
import com.kenzie.appserver.service.model.UserProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/example")
public class UserController {

    private UserProfileService userService;

    UserController(UserProfileService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") String id) {

        UserProfile user = userService.getProfile(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse response = createResponseFromProfile(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse> addNewUser(@RequestBody UserCreateRequest request) {
        UserProfile user = userService.addProfile(createProfileFromRequest(request));

        UserResponse response = createResponseFromProfile(user);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserCreateRequest request) {
        UserProfile user = userService.getProfile(request.getId());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userService.updateProfile(createProfileFromRequest(request));
        UserResponse response = createResponseFromProfile(user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("id") String id) {
        UserProfile user = userService.getProfile(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponse response = createResponseFromProfile(user);
        userService.deleteUserProfile(id);
        return ResponseEntity.ok(response);
    }

    private UserProfile createProfileFromRequest(UserCreateRequest request) {
        UserProfile user = new UserProfile(request.getId(),
                                            request.getUsername(),
                                            request.getDietaryRestrictions(),
                                            request.getFavoriteRecipes());

        return user;

    }

    private UserResponse createResponseFromProfile(UserProfile profile) {
        UserResponse response = new UserResponse();
        response.setId(profile.getId());
        response.setUsername(profile.getUsername());
        response.setDietaryRestrictions(profile.getDietaryRestrictions());
        response.setFavoriteRecipes(profile.getFavoriteRecipes());
        return response;
    }
}
