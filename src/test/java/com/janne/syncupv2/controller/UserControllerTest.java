package com.janne.syncupv2.controller;

import com.janne.syncupv2.Application;
import com.janne.syncupv2.model.dto.RegisterUserRequestDto;
import com.janne.syncupv2.model.user.User;
import com.janne.syncupv2.repository.UserRepository;
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

    @Test
    public void givenRegisteredUser_checkDatabase() throws Exception {
        //Arrange
        RegisterUserRequestDto registerUserRequestDto = RegisterUserRequestDto.builder()
                .email("test@test.de")
                .password("test1234")
                .name("testi")
                .build();

        //Act
        mvc.perform(post(BASE_PATH + "/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerUserRequestDto)))
                .andExpect(status().isCreated());

        //Assert
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
        User user = users.get(0);
        assertThat(user.getEmail()).isEqualTo("test@test.de");
        System.out.println(user.getPassword() + registerUserRequestDto.getPassword() + passwordEncoder.matches(registerUserRequestDto.getPassword(), user.getPassword()));
        assertThat(passwordEncoder.matches(registerUserRequestDto.getPassword(), user.getPassword())).isTrue();
        assertThat(user.getName()).isEqualTo("testi");
    }
}
