package com.janne.syncupv2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.janne.syncupv2.Application;
import com.janne.syncupv2.model.dto.incomming.requests.auth.RegisterUserRequest;
import com.janne.syncupv2.model.dto.outgoing.AuthenticationResponse;
import com.janne.syncupv2.model.dto.outgoing.user.PrivateUserDto;
import com.janne.syncupv2.model.dto.outgoing.user.PublicUserDto;
import com.janne.syncupv2.model.jpa.user.UserRole;
import com.janne.syncupv2.repository.TokenRepository;
import com.janne.syncupv2.repository.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.janne.syncupv2.TestUtil.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserManagementTest {


    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TokenRepository tokenRepository;

    @After
    public void tearDown() throws Exception {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void givenUserRegisterRequest_checkExistence() throws Exception {
        //Arrange
        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .email("testuserTEST@test.test")
                .password("test1234")
                .usertag("gamer1234")
                .build();

        //Act
        AuthenticationResponse authenticationResponse = parseResponseAction(registerUser(mvc, registerUserRequest).andExpect(status().isCreated()), objectMapper, AuthenticationResponse.class);
        PrivateUserDto privateUserDto = parseResponseAction(getAuthorizedUserDetails(mvc, authenticationResponse.getAccessToken()), objectMapper, PrivateUserDto.class);
        PublicUserDto publicUserDto = parseResponseAction(getUnauthorizedUserDetails(mvc, authenticationResponse.getUserId()), objectMapper, PublicUserDto.class);

        //Assert
        assertThat(privateUserDto.getUsertag()).isEqualTo(registerUserRequest.getUsertag());
        assertThat(privateUserDto.getEmail()).isEqualTo(registerUserRequest.getEmail().toLowerCase());
        assertThat(privateUserDto.getRole()).isEqualTo(UserRole.USER.toString());
        assertThat(privateUserDto.getId()).isEqualTo(authenticationResponse.getUserId());

        assertThat(publicUserDto.getUsertag()).isEqualTo(registerUserRequest.getUsertag());
        assertThat(publicUserDto.getRole()).isEqualTo(UserRole.USER.toString());
        assertThat(publicUserDto.getId()).isEqualTo(authenticationResponse.getUserId());
    }

    @Test
    public void givenInvalidUserRequest_expectError() throws Exception {
        //Arrange
        List<RegisterUserRequest> invalidRegisterUserRequestDtos = List.of(
                RegisterUserRequest.builder()
                        .usertag("test test")
                        .email("test")
                        .password("test1234")
                        .build(),
                RegisterUserRequest.builder()
                        .usertag("hih")
                        .email("test@gmail.com")
                        .password("test1234")
                        .build(),
                RegisterUserRequest.builder()
                        .email("test@gmail.com")
                        .usertag("testtest")
                        .password("tosmall")
                        .build(),
                RegisterUserRequest.builder()
                        .email("test@gmail.com")
                        .password("test1234")
                        .build(),
                RegisterUserRequest.builder()
                        .email("test@test.de")
                        .password("test1234")
                        .build(),
                RegisterUserRequest.builder()
                        .usertag("testname")
                        .password("test1234")
                        .build()
        );

        //Act + Assert
        for (RegisterUserRequest invalidRegisterUserRequestDto : invalidRegisterUserRequestDtos) {
            registerUser(mvc, invalidRegisterUserRequestDto).andExpect(status().isBadRequest());
        }
    }

    @Test
    public void givenUserDeletionRequest_checkExistence() throws Exception {
        //Arrange
        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .email("testuserTEST@test.test")
                .password("test1234")
                .usertag("gamer1234")
                .build();

        AuthenticationResponse authenticationResponse = parseResponseAction(registerUser(mvc, registerUserRequest), objectMapper, AuthenticationResponse.class);

        //Act
        PrivateUserDto deletedUserDto = parseResponseAction(deleteUser(mvc, authenticationResponse.getAccessToken()), objectMapper, PrivateUserDto.class);

        //Assert
        assertThat(deletedUserDto.getUsertag()).isEqualTo(registerUserRequest.getUsertag());
        assertThat(deletedUserDto.getEmail()).isEqualTo(registerUserRequest.getEmail().toLowerCase());
        assertThat(deletedUserDto.getId()).isEqualTo(authenticationResponse.getUserId());
        assertThat(deletedUserDto.getUsertag()).isEqualTo(registerUserRequest.getUsertag());
        assertThat(deletedUserDto.getRole()).isEqualTo(UserRole.USER.toString());
        ResultActions res = getUnauthorizedUserDetails(mvc, authenticationResponse.getUserId()).andExpect(status().isNotFound());
        System.out.println(res.andReturn().getResponse());
        getAuthorizedUserDetails(mvc, authenticationResponse.getAccessToken()).andExpect(status().isForbidden());
    }
}
