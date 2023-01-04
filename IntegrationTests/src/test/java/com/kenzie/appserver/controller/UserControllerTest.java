package com.kenzie.appserver.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


//@IntegrationTest
//class UserControllerTest {
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    ExampleService exampleService;
//
//    private final MockNeat mockNeat = MockNeat.threadLocal();
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @Test
//    public void getById_Exists() throws Exception {
//
//        String name = mockNeat.strings().valStr();
//
//        Example persistedExample = exampleService.addNewExample(name);
//        mvc.perform(get("/example/{id}", persistedExample.getId())
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("id")
//                        .isString())
//                .andExpect(jsonPath("name")
//                        .value(is(name)))
//                .andExpect(status().is2xxSuccessful());
//    }
//
//    @Test
//    public void createExample_CreateSuccessful() throws Exception {
//        String name = mockNeat.strings().valStr();
//
//        UserCreateRequest userCreateRequest = new UserCreateRequest();
//        userCreateRequest.setName(name);
//
//        mapper.registerModule(new JavaTimeModule());
//
//        mvc.perform(post("/example")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(userCreateRequest)))
//                .andExpect(jsonPath("id")
//                        .exists())
//                .andExpect(jsonPath("name")
//                        .value(is(name)))
//                .andExpect(status().is2xxSuccessful());
//    }
//}