package com.kenzie.appserver;

import com.kenzie.appserver.controller.model.UserCreateRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class QueryUtility {

    public UserControllerClient userControllerClient;

    private final MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    public QueryUtility(MockMvc mvc) {
        this.mvc = mvc;
        this.userControllerClient = new UserControllerClient();
    }

    public class UserControllerClient {
        public ResultActions getUser(String username) throws Exception {
            return mvc.perform(get("/user/{username}", username)
                    .accept(MediaType.APPLICATION_JSON));
        }

        public ResultActions addNewUser(UserCreateRequest request) throws Exception {
            return mvc.perform(post("/user")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)));
        }

        public ResultActions deleteUser(String username) throws Exception {
            return mvc.perform(delete("/user/{username}", username)
                    .accept(MediaType.APPLICATION_JSON));
        }

        public ResultActions updateUser(UserCreateRequest request) throws Exception {
            return mvc.perform(put("/user")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)));
        }

    }
}
