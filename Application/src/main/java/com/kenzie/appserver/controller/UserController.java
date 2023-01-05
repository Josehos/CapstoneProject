package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;

import com.kenzie.appserver.service.RecipeService;
import com.kenzie.appserver.service.UserProfileService;
import com.kenzie.appserver.service.model.UserProfile;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserProfileService userService;
    private RecipeService recipeService;
    private LambdaServiceClient client;

    UserController(UserProfileService userService, RecipeService recipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
        this.client = new LambdaServiceClient();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("username") String username) {

        UserProfile user = userService.getProfile(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse response = createResponseFromProfile(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}/restrictions")
    public String getUserRestrictions(@PathVariable("username") String username) {

        UserProfile user = userService.getProfile(username);
        if (user == null) {
            return HttpStatus.BAD_REQUEST.toString();
        }

        return "  ";
    }

    @PostMapping
    public ResponseEntity<UserResponse> addNewUser(@RequestBody UserCreateRequest request) {
        UserProfile user = userService.addProfile(createProfileFromRequest(request));

        UserResponse response = createResponseFromProfile(user);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserCreateRequest request) {
        UserProfile user = userService.getProfile(request.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userService.updateProfile(createProfileFromRequest(request));
        UserResponse response = createResponseFromProfile(user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("username") String username) {
        UserProfile user = userService.getProfile(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponse response = createResponseFromProfile(user);
        userService.deleteUserProfile(username);
        return ResponseEntity.ok(response);
    }

    private UserProfile createProfileFromRequest(UserCreateRequest request) {
        UserProfile user = new UserProfile(request.getUsername(),
                                            request.getDietaryRestrictions(),
                                            request.getFavoriteRecipes());

        return user;

    }

    private UserResponse createResponseFromProfile(UserProfile profile) {
        UserResponse response = new UserResponse();
        response.setUsername(profile.getUsername());
        response.setDietaryRestrictions(profile.getDietaryRestrictions());
        response.setFavoriteRecipes(profile.getFavoriteRecipes());
        return response;
    }
}
