package com.skyteam.skygram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyteam.skygram.config.CorsFilter;
import com.skyteam.skygram.dto.LoginDTO;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.security.JwtAuthenticationEntryPoint;
import com.skyteam.skygram.security.JwtAuthenticationFilter;
import com.skyteam.skygram.security.JwtTokenProvider;
import com.skyteam.skygram.security.JwtUserDetailsService;
import com.skyteam.skygram.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UserService userService;

    @MockBean
    private CorsFilter corsFilter;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testLogin_success() throws Exception {
        LoginDTO body = new LoginDTO("account", "password");
        mvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(body)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testRegister_success() throws Exception {
        UserRequestDTO body = new UserRequestDTO(
                "skyteam",
                "Sky",
                "Team",
                "skyteam",
                "123456789",
                LocalDate.of(2000, 1, 1),
                "123456"
        );
        when(userService.addUser(body)).thenReturn(new UserDTO());
        mvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(body)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
