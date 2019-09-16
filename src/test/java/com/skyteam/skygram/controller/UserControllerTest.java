package com.skyteam.skygram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.security.JwtAuthenticationEntryPoint;
import com.skyteam.skygram.security.JwtTokenProvider;
import com.skyteam.skygram.security.JwtUserDetailsService;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.PostService;
import com.skyteam.skygram.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper mapper = new ObjectMapper();

    @Mock
    private UserPrincipal currentUser;

    @WithMockUser
    @Test
    public void testGetListUsers_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @WithMockUser
    @Test
    public void testUpdateProfile_success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("skyteam");
        userDTO.setFirstName("Sky");
        userDTO.setLastName("Team");
        userDTO.setEmail("skyteam@gmail.com");
        mvc.perform(MockMvcRequestBuilders.put("/users/me/profile")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").value("User is updated"));
    }

    @WithMockUser
    @Test
    public void testFollow_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users/toandc/follow"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @WithMockUser
    @Test
    public void testUnfollow_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users/toandc/unfollow"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @WithMockUser
    @Test
    public void testGetUserProfile_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users/toandc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @WithMockUser
    @Test
    public void testGetPostsByUsername_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users/toandc/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }
}
