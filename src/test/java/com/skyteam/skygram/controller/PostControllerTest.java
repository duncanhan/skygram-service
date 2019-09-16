package com.skyteam.skygram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyteam.skygram.dto.CommentRequestDTO;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {

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
    public void testCreatePost_success() throws Exception {
//        MockMultipartFile image = new MockMultipartFile("media", "image.jpg", "image/jpeg", "image".getBytes());
//        MockMultipartFile title = new MockMultipartFile("title", "title", "text/plain", "title".getBytes());
//        mvc.perform(MockMvcRequestBuilders.multipart("/posts")
//                .file("media", image.getBytes())
//                .file("title", title.getBytes()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("success"));
    }

    @WithMockUser
    @Test
    public void testUpdatePost_success() throws Exception {
//        MultipartFile file = mock(MultipartFile.class);
//        mvc.perform(MockMvcRequestBuilders.post("/posts")
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .content(file.getBytes())
//                .param("title", "new post"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("success"));
    }

    @WithMockUser
    @Test
    public void testDeletePost_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/posts/5d7d6c9c37eae66dda9e307d"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Post is deleted"));
    }

    @WithMockUser
    @Test
    public void testCreateComment_success() throws Exception {
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("this is a comment");
        mvc.perform(MockMvcRequestBuilders.post("/posts/5d7d6c9c37eae66dda9e307d/comments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(commentRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @WithMockUser
    @Test
    public void testUpdateComment_success() throws Exception {
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("this is a comment");
        mvc.perform(MockMvcRequestBuilders.put("/posts/5d7d6c9c37eae66dda9e307d/comments/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(commentRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @WithMockUser
    @Test
    public void testDeleteComment_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/posts/5d7d6c9c37eae66dda9e307d/comments/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comment is deleted"));
    }

    @WithMockUser
    @Test
    public void testLikePost_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/posts/5d7d6c9c37eae66dda9e307d/like"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @WithMockUser
    @Test
    public void testUnlikePost_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/posts/5d7d6c9c37eae66dda9e307d/unlike"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @WithMockUser
    @Test
    public void testGetTimelinePost_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/posts/timeline"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @WithMockUser
    @Test
    public void testGetPostDetail_success() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/posts/5d7d6c9c37eae66dda9e307d"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));
    }
}
