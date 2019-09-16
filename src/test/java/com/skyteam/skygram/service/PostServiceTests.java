package com.skyteam.skygram.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.skyteam.skygram.dto.CommentDTO;
import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.dto.PostDTO;
import com.skyteam.skygram.exception.NoPermissionException;
import com.skyteam.skygram.exception.ResourceNotFoundException;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.repository.PostRepository;
import com.skyteam.skygram.repository.UserRepository;
import com.skyteam.skygram.security.UserPrincipal;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.not;
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

    private Post post;
    private User author;
    private UserPrincipal currentUser;
    private static final int PAGE = 0;
    private static final int SIZE = 15;
    private static final String POST_ID = "5d7d6c9c37eae66dda9e307d";
    private static final String AUTHOR_ID = "author";
    private static final String COMMENT_ID = "5d7e4b3224aa9a0001f18bb9";
    private Pageable pageRequest = PageRequest.of(PAGE, SIZE);

    @BeforeClass
    public static void beforeClass() throws Exception {

    }

    @Before
    public void setUp() throws Exception {
        currentUser = mock(UserPrincipal.class);
        when(currentUser.getId()).thenReturn(AUTHOR_ID);
        author = new User(AUTHOR_ID);
        author.setUsername("skyteam");
        post = new Post();
        post.setId(POST_ID);
        post.setAuthor(author);
        post.setTitle("Hello SkyGram");
    }

    @Test
    public void testGetPostsByUser() {
        Page<Post> page = new PageImpl<>(Collections.singletonList(post), pageRequest, 1);
        when(postRepository.findByAuthor(anyString(), eq(pageRequest))).thenReturn(page);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(author));
        Page<PostDTO> actual = postService.getPostsByUser(currentUser, "user", pageRequest);
        assertEquals(0, actual.getNumber());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getContent().size());
        assertEquals(POST_ID, actual.getContent().get(0).getId());
        assertEquals("Hello SkyGram", actual.getContent().get(0).getTitle());
    }

    @Test
    public void testCreatePost() throws IOException {
//        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "some xml".getBytes());
//        Map<String, String> res = new HashMap<>();
//        res.put("url", "image_url");
//        res.put("format", "jpg");
//        when(cloudinary.uploader()).thenReturn(new Uploader(cloudinary, null));
//        assertNull(cloudinary.uploader());
//        when(cloudinary.uploader().upload(image.getBytes(), null)).thenReturn(res);
//        PostDTO postDTO = postService.createPost(currentUser, "post", new MultipartFile[]{image}, null, null);
//        assertNull(postDTO);
    }

    @Test
    public void testUpdatePost() {
        //TODO
    }

    @Test
    public void testDeletePost_success() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        assertTrue(postService.deletePost(currentUser, POST_ID));
    }

    @Test(expected = NoPermissionException.class)
    public void testDeletePost_noPermission() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        User author1 = new User("author2");
        post.setAuthor(author1);
        postService.deletePost(currentUser, POST_ID);
    }

    @Test
    public void testCreateComment() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("comment");
        CommentDTO commentDTO = postService.createComment(currentUser, POST_ID, commentRequestDTO);
        assertFalse(post.getComments().isEmpty());
        assertEquals("comment", commentDTO.getText());
        assertEquals(AUTHOR_ID, commentDTO.getAuthor().getId());
    }

    @Test
    public void testUpdateComment_success() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("comment");
        CommentDTO commentDTO = postService.createComment(currentUser, POST_ID, commentRequestDTO);
        commentRequestDTO.setText("new comment");
        assertTrue(postService.updateComment(currentUser, POST_ID, post.getComments().get(0).getId().toString(), commentRequestDTO));
        assertFalse(post.getComments().isEmpty());
        assertEquals("comment", commentDTO.getText());
        assertEquals(AUTHOR_ID, commentDTO.getAuthor().getId());
    }

    @Test(expected = NoPermissionException.class)
    public void testUpdateComment_noPermission() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("comment");
        CommentDTO commentDTO = postService.createComment(currentUser, POST_ID, commentRequestDTO);
        commentRequestDTO.setText("new comment");
        when(currentUser.getId()).thenReturn("author1");
        postService.updateComment(currentUser, POST_ID, post.getComments().get(0).getId().toString(), commentRequestDTO);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateComment_commentNotFound() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("comment");
        CommentDTO commentDTO = postService.createComment(currentUser, POST_ID, commentRequestDTO);
        commentRequestDTO.setText("new comment");
        postService.updateComment(currentUser, POST_ID, new ObjectId().toString(), commentRequestDTO);
    }

    @Test
    public void testDeleteComment_success() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("comment");
        CommentDTO commentDTO = postService.createComment(currentUser, POST_ID, commentRequestDTO);
        assertTrue(postService.deleteComment(currentUser, POST_ID, post.getComments().get(0).getId().toString()));
    }

    @Test(expected = NoPermissionException.class)
    public void testDeleteComment_noPermission() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("comment");
        CommentDTO commentDTO = postService.createComment(currentUser, POST_ID, commentRequestDTO);
        when(currentUser.getId()).thenReturn("author1");
        postService.deleteComment(currentUser, POST_ID, post.getComments().get(0).getId().toString());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteComment_commentNotFound() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("comment");
        CommentDTO commentDTO = postService.createComment(currentUser, POST_ID, commentRequestDTO);
        postService.deleteComment(currentUser, POST_ID, new ObjectId().toString());
    }

    @Test
    public void testLike() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        assertTrue(postService.like(currentUser, POST_ID));
        assertThat(post.getLikes(), IsCollectionContaining.hasItem(AUTHOR_ID));
    }

    @Test
    public void tesUnlike() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        assertTrue(postService.like(currentUser, POST_ID));
        assertThat(post.getLikes(), IsCollectionContaining.hasItem(AUTHOR_ID));
        assertTrue(postService.unlike(currentUser, POST_ID));
        assertThat(post.getLikes(), not(IsCollectionContaining.hasItem(AUTHOR_ID)));
    }

    @Test
    public void testGetTimelinePosts() throws Exception {
        Page<Post> page = new PageImpl<>(Collections.singletonList(post), pageRequest, 1);
        List<String> lists = Arrays.asList("user1", "user2");
        Document document = new Document("followings", Arrays.asList("user1", "user2"));
        when(userRepository.findFollowings(anyString())).thenReturn(document);
        List<String> lists1 = new ArrayList<>();
        lists1.add(AUTHOR_ID);
        lists1.addAll(lists);
        when(postRepository.findByAuthorIn(lists1, pageRequest)).thenReturn(page);
        Page<PostDTO> actual = postService.getTimelinePosts(currentUser, pageRequest);
        assertEquals(0, actual.getNumber());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getContent().size());
        assertEquals(POST_ID, actual.getContent().get(0).getId());
        assertEquals("Hello SkyGram", actual.getContent().get(0).getTitle());
    }

    @Test
    public void testGetPostDetail() throws Exception {
        when(postRepository.findById(anyString())).thenReturn(Optional.of(post));
        post.likedBy(AUTHOR_ID);
        PostDTO postDTO = postService.getPostDetail(currentUser, POST_ID);
        assertEquals(POST_ID, postDTO.getId());
        assertEquals("Hello SkyGram", postDTO.getTitle());
        assertTrue(postDTO.isLiked());
        assertTrue(postDTO.isOwned());
    }

    @Test
    public void testGetNumOfPosts() throws Exception {
        LocalDate date = LocalDate.of(2019, 9, 16);
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
        when(postRepository.countByPostedDateBetween(start, end)).thenReturn(10L);
        assertEquals(10, postService.getNumOfPosts(date));
    }
}
