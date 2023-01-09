package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.QueryUtility;
import com.kenzie.appserver.TestUtility;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.service.UserProfileService;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    UserProfileService userProfileService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    private QueryUtility queryUtility;
    private TestUtility testUtil;

    @BeforeAll
    public void setup() {
        queryUtility = new QueryUtility(mvc);
        testUtil = new TestUtility(mvc, queryUtility);
    }


    @Test
    public void createUser_CreateSuccessful() throws Exception {
        UserCreateRequest request = testUtil.createSingleRequest();

        queryUtility.userControllerClient.addNewUser(request)
                .andExpect(jsonPath("username")
                        .value(is(request.getUsername())))
                .andExpect(status().isOk());
    }
    @Test
    public void getByUsername_Exists() throws Exception {
        UserCreateRequest request = testUtil.createSingleRequest();

        queryUtility.userControllerClient.addNewUser(request);
        queryUtility.userControllerClient.getUser(request.getUsername())
                .andExpect(jsonPath("username")
                        .value(is(request.getUsername())))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUser_updateSuccessful() throws Exception {
        UserCreateRequest request = testUtil.createSingleRequest();

        queryUtility.userControllerClient.addNewUser(request)
                .andExpect(jsonPath("username")
                        .value(is(request.getUsername())))
                .andExpect(status().isOk());

        List<String> dietaryRestrictions = request.getDietaryRestrictions();
        if (!dietaryRestrictions.contains("egg")) {
            dietaryRestrictions.add("egg");
        } else if (!dietaryRestrictions.contains("dairy")) {
            dietaryRestrictions.add("dairy");
        } else {
            dietaryRestrictions.add("gluten");
        }

        request.setDietaryRestrictions(dietaryRestrictions);

        queryUtility.userControllerClient.updateUser(request)
                        .andExpect(status().isOk());

        queryUtility.userControllerClient.getUser(request.getUsername())
                .andExpect(jsonPath("username")
                        .value(is(request.getUsername())))
                .andExpect(jsonPath("dietaryRestrictions")
                        .value(is(request.getDietaryRestrictions())))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser_deleteSuccessful() throws Exception {
        UserCreateRequest request = testUtil.createSingleRequest();

        queryUtility.userControllerClient.addNewUser(request)
                .andExpect(jsonPath("username")
                        .value(is(request.getUsername())))
                .andExpect(status().isOk());

        queryUtility.userControllerClient.getUser(request.getUsername())
                .andExpect(jsonPath("username")
                        .value(is(request.getUsername())))
                .andExpect(status().isOk());

        queryUtility.userControllerClient.deleteUser(request.getUsername())
                .andExpect(status().isOk());

        queryUtility.userControllerClient.getUser(request.getUsername())
                .andExpect(status().is4xxClientError());
    }
}