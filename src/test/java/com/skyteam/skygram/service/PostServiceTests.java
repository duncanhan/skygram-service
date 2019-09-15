package com.skyteam.skygram.service;

import com.cloudinary.Cloudinary;
import com.skyteam.skygram.dto.PostDTO;
import com.skyteam.skygram.exception.NoPermissionException;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.repository.PostRepository;
import com.skyteam.skygram.repository.UserRepository;
import com.skyteam.skygram.security.UserPrincipal;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostServiceTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private Cloudinary cloudinary;

    @Autowired
    private PostService postService;

    private static Post post;
    private static User author;
    private static UserPrincipal userPrincipal;
    private static final int PAGE = 0;
    private static final int SIZE = 15;
    private static Pageable pageRequest;

    @BeforeClass
    public static void beforeClass() throws Exception {
        userPrincipal = mock(UserPrincipal.class);
        when(userPrincipal.getId()).thenReturn("author");
        author = new User("author");
        author.setUsername("skyteam");
        pageRequest = PageRequest.of(PAGE, SIZE);
        post = new Post();
        post.setId("post");
        post.setAuthor(author);
        post.setTitle("Hello SkyGram");
    }

    @Test
    public void testGetPostsByUser() {
        Page<Post> emptyPage = new PageImpl<>(Collections.singletonList(post), pageRequest, 1);
        when(postRepository.findByAuthor(anyString(), eq(pageRequest))).thenReturn(emptyPage);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(author));
        Page<PostDTO> actual = postService.getPostsByUser(userPrincipal, "user", pageRequest);
        assertEquals(0, actual.getNumber());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getContent().size());
        assertEquals("post", actual.getContent().get(0).getId());
        assertEquals("Hello SkyGram", actual.getContent().get(0).getTitle());
    }


    @Test
    public void testCreatePost() {
        //TODO
    }

    @Test
    public void testUpdatePost() {
        //TODO
    }

    @Test
    public void testDeletePost_success() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        assertTrue(postService.deletePost(userPrincipal, "post"));
    }

    @Test(expected = NoPermissionException.class)
    public void testDeletePost_noPermission() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        User author1 = new User("author2");
        post.setAuthor(author1);
        postService.deletePost(userPrincipal, "post");
    }
}
