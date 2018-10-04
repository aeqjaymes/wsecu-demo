package com.aequilibrium.wsecu.controller;

import com.aequilibrium.wsecu.data.entity.User;
import com.aequilibrium.wsecu.data.repository.UserRepository;
import com.aequilibrium.wsecu.dto.UserCreate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Example suite of integration tests
 *
 * Note: This is not a full test suite. This is purely an example of various types of testing
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAllInBatch();
        userRepository.flush();
    }

    @Test
    public void shouldReturnUserNotFoundErrorWhenSuppliedWithAMissingUsername() throws Exception {
        mockMvc.perform(get("/user").param("username", "invalid-username"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(is("User not found")));
    }

    @Test
    public void shouldReturnUserNotFoundErrorWhenSuppliedWithAnInvalidUserId() throws Exception {
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(is("User not found")));
    }

    @Test
    public void shouldReturnUserNotFoundErrorWhenSuppliedWithAnInvalidUserIdWhenDeletingAUser() throws Exception {
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(is("User not found")));
    }

    @Test
    public void shouldCreateAUser() throws Exception {
        UserCreate userCreate = getTestUser();

        // Note: Typically would do a schema match (via json schema or the like)
        mockMvc.perform(post("/user/").content(toJson(userCreate)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber());
    }

    private UserCreate getTestUser() {
        UserCreate userCreate = new UserCreate();
        userCreate.setEmail("test@email.com");
        userCreate.setUsername("test-user");
        userCreate.setFirstName("Test");
        userCreate.setLastName("User");
        userCreate.setPassword("my test password");
        return userCreate;
    }

    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    public void shouldReturnUserDetails() throws Exception {
        UserCreate userCreate = getTestUser();

        mockMvc.perform(post("/user/").content(toJson(userCreate)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    public void shouldDeleteAnExistingUser() throws Exception {
        UserCreate userCreate = getTestUser();

        MvcResult mvcResult = mockMvc.perform(post("/user/").content(toJson(userCreate)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        User user = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);

        mockMvc.perform(delete("/user/" + user.getId()))
                .andExpect(status().isNoContent());
    }
}
