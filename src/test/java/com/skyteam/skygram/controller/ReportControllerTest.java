package com.skyteam.skygram.controller;

import com.skyteam.skygram.security.JwtAuthenticationEntryPoint;
import com.skyteam.skygram.security.JwtTokenProvider;
import com.skyteam.skygram.security.JwtUserDetailsService;
import com.skyteam.skygram.service.PostService;
import com.skyteam.skygram.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReportController.class)
public class ReportControllerTest {

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

    @WithMockUser
    @Test
    public void testGetReport_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/reports")
                .param("from", "2019-09-11")
                .param("to", "2019-09-16"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data", hasSize(6)))
                .andExpect(jsonPath("$.data[0].date").value("2019-09-11"))
                .andExpect(jsonPath("$.data[0].num_of_registrations").value(0))
                .andExpect(jsonPath("$.data[0].num_of_posts").value(0))
                .andExpect(jsonPath("$.data[5].date").value("2019-09-16"))
                .andExpect(jsonPath("$.data[0].num_of_registrations").value(0))
                .andExpect(jsonPath("$.data[0].num_of_posts").value(0));
    }
}
