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

import static com.kenzie.appserver.Utilties.ConverterUtilities.createProfileFromRequest;
import static com.kenzie.appserver.Utilties.ConverterUtilities.createResponseFromProfile;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

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
        UserProfile user;
        try {
            user = userService.getProfile(username);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        // if (user == null) {
        //     return ResponseEntity.notFound().build();
        //     // return null;
        // }

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
        UserProfile user = userService.updateProfile(createProfileFromRequest(request));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponse response = createResponseFromProfile(user);
        return ResponseEntity.ok(response);
    }

//    @RequestMapping(method = {RequestMethod.DELETE, RequestMethod.GET})
//    @ResponseBody
    @DeleteMapping("/{username}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("username") String username) {
        UserProfile user = userService.deleteUserProfile(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponse response = createResponseFromProfile(user);
        return ResponseEntity.ok(response);
        // UserProfile user;
        // try {
        //     user = userService.getProfile(username);
        // } catch (Exception e) {
        //     return ResponseEntity.notFound().build();
        // }
        // UserResponse response = createResponseFromProfile(user);
        // userService.deleteUserProfile(username);
        // return ResponseEntity.ok(response);
    }


}
