package com.kenzie.appserver;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.kenzie.appserver.dao.UserProfileDao;
import com.kenzie.appserver.service.UserProfileService;
import com.kenzie.appserver.service.model.UserProfile;

import java.util.List;

public class Main {

    public static void main(String args[]) {
        UserProfileDao userProfileDao = new UserProfileDao(new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient()));
        UserProfileService service = new UserProfileService(userProfileDao);
        List<String> restrictions = List.of("dairy", "eggs");
        List<String> recipes = List.of("pizza", "burgers");
        service.addProfile(new UserProfile("teamKenzie", restrictions, recipes));
        System.out.println(service.getProfile("teamKenzie"));

    }
}
