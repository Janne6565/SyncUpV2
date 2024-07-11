package com.janne.syncupv2.controller;

import com.janne.syncupv2.Application;
import com.janne.syncupv2.model.dto.RegisterUserRequestDto;
import com.janne.syncupv2.model.user.User;
import com.janne.syncupv2.repository.UserRepository;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.janne.syncupv2.TestUtil.asJsonString;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    private static final String BASE_PATH = "/v1";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Contract("_, _ -> new")
    private @NotNull ResultActions registerUser(@NotNull MockMvc mockMvc, RegisterUserRequestDto registerUserRequestDto) throws Exception {
        return mockMvc.perform(post(BASE_PATH + "/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerUserRequestDto)));
    }

    private void assertEqual(@org.jetbrains.annotations.NotNull User actual, @NotNull RegisterUserRequestDto expected) throws Exception {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(passwordEncoder.matches(expected.getPassword(), actual.getPassword())).isTrue();
    }

    @Test
    public void givenRegisteredUser_checkDatabase() throws Exception {
        //Arrange
        RegisterUserRequestDto registerUserRequestDto = RegisterUserRequestDto.builder()
                .email("test@test.de")
                .password("test1234")
                .name("test testi").build();

        //Act
        registerUser(mvc, registerUserRequestDto).andExpect(status().isCreated());

        //Assert
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
        assertEqual(users.get(0), registerUserRequestDto);
    }

    @Test
    public void givenTwoRegisteredUsers_expectError() throws Exception {
        //Arrange
        RegisterUserRequestDto registerUserRequestDto1 = RegisterUserRequestDto.builder()
                .email("test@gmail.com")
                .password("testtest")
                .name("hallihallo :)")
                .build();
        RegisterUserRequestDto registerUserRequestDto2 = RegisterUserRequestDto.builder()
                .email("TEST@gmail.com")
                .password("testtest")
                .name("hallihallo")
                .build();

        //Act
        registerUser(mvc, registerUserRequestDto1).andExpect(status().isCreated());
        registerUser(mvc, registerUserRequestDto2).andExpect(status().isBadRequest());

        //Assert
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
        assertEqual(users.get(0), registerUserRequestDto1);
    }

    @Test
    public void givenInvalidUserRegisterRequest_expectError() throws Exception {
        //Arrange
        List<RegisterUserRequestDto> invalidRegisterUserRequestDtos = List.of(
                RegisterUserRequestDto.builder()
                        .name("test test")
                        .email("test")
                        .password("test1234")
                        .build(),
                RegisterUserRequestDto.builder()
                        .name("hih")
                        .email("test@gmail.com")
                        .password("test1234")
                        .build(),
                RegisterUserRequestDto.builder()
                        .email("test@gmail.com")
                        .name("testtest")
                        .password("tosmall")
                        .build(),
                RegisterUserRequestDto.builder()
                        .email("test@gmail.com")
                        .password("test1234")
                        .build(),
                RegisterUserRequestDto.builder()
                        .email("test@test.de")
                        .password("test1234")
                        .build(),
                RegisterUserRequestDto.builder()
                        .name("testname")
                        .password("test1234")
                        .build()
        );

        //Act
        for (RegisterUserRequestDto registerUserRequestDto : invalidRegisterUserRequestDtos) {
            registerUser(mvc, registerUserRequestDto).andExpect(status().isBadRequest());
        }

        //Assert
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(0);
    }
}
