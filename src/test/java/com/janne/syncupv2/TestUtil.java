package com.janne.syncupv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.janne.syncupv2.model.dto.incomming.requests.auth.RegisterUserRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class TestUtil {
    private static final String BASE_PATH = "/v1";
    private static final String AUTH_BASE_PATH = BASE_PATH + "/auth";
    private static final String AUTHORIZED_BASE_PATH = BASE_PATH + "/authorized";
    private static final String UNAUTHORIZED_BASE_PATH = BASE_PATH + "/unauthorized";

    public static @NotNull ResultActions registerUser(@NotNull MockMvc mockMvc, RegisterUserRequest registerUserRequest) throws Exception {
        return mockMvc.perform(post(AUTH_BASE_PATH + "/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerUserRequest)));
    }

    public static @NotNull ResultActions getAuthorizedUserDetails(@NotNull MockMvc mockMvc, String authToken) throws Exception {
        return mockMvc.perform(get(AUTHORIZED_BASE_PATH + "/user")
                .header("Authorization", "Bearer " + authToken));
    }


    public static @NotNull ResultActions deleteUser(@NotNull MockMvc mockMvc, String authToken) throws Exception {
        return mockMvc.perform(delete(AUTHORIZED_BASE_PATH + "/user")
                .header("Authorization", "Bearer " + authToken));
    }

    public static @NotNull ResultActions getUnauthorizedUserDetails(@NotNull MockMvc mockMvc, Integer userId) throws Exception {
        return mockMvc.perform(get(UNAUTHORIZED_BASE_PATH + "/user/" + userId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public static <T> T parseResponseAction(ResultActions result, ObjectMapper objectMapper, Class<T> classParsing) throws Exception {
        return objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), classParsing);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
