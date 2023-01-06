package com.kenzie.appserver.controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.service.UserProfileService;
import com.kenzie.appserver.service.model.UserProfile;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    UserProfileService userProfileService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getByUsername_Exists() throws Exception {

        UserProfile user = new UserProfile();
        user.setUsername("testUsername");

        UserProfile persistedUser = userProfileService.addProfile(user);
        mvc.perform(get("/user/{username}", persistedUser.getUsername())
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("id")
                        .isString())
                .andExpect(jsonPath("name")
                        .value(is(user.getUsername())))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void createExample_CreateSuccessful() throws Exception {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUsername("testUsername");

//        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/example")
                        .accept(String.valueOf(MediaType.APPLICATION_JSON))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(mapper.writeValueAsString(userCreateRequest)))
                .andExpect(jsonPath("name")
                        .value(is(userCreateRequest.getUsername())))
                .andExpect(status().is2xxSuccessful());
    }
}