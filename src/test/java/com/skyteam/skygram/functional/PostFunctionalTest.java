package com.skyteam.skygram.functional;

import com.skyteam.skygram.model.Comment;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.PostService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.skyteam.skygram.functional.PostFunctional.MOST_LIKED_K_POSTS;
import static com.skyteam.skygram.functional.PostFunctional.TOP_K_COMMENTS_BY_LENGTH_FOR_USER_ON_DATE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostFunctionalTest {
    List<Comment> cmts;
    @Autowired
    private PostService postService;
    private UserPrincipal currentUser;
    private User author;
    private Post post;
    private static final String POST_ID = "5d7d6c9c37eae66dda9e307d";
    private static final String AUTHOR_ID = "author";
    private static final String COMMENT_ID = "5d7e4b3224aa9a0001f18bb9";
    private static List<Post> posts;
    private static User user;

    @BeforeClass
    public static void beforeClass() throws Exception {
        posts = spy(new ArrayList<>());

        user = mock(User.class);
        user.setEmail("sky@skyteam.com");
        when(user.getFollowings()).thenReturn(new HashSet<>(Arrays.asList("user2")));
        Post post1 = mock(Post.class);
        when(post1.getAuthor()).thenReturn(user);
        when(post1.getPostedDate()).thenReturn(LocalDateTime.of(2010, 9, 23, 10, 0, 0));
        when(post1.getHashtags()).thenReturn(new HashSet<>(Arrays.asList("#hoping", "#sleep")));
        when(post1.getNumOfLikes()).thenReturn(10);
        posts.add(post1);
        verify(posts).add(post1);

        User user2 = mock(User.class);
        when(user2.getId()).thenReturn("user2");
        when(user2.getFollowings()).thenReturn(new HashSet<>(Arrays.asList("user")));
        Post post2 = mock(Post.class);
        when(post2.getAuthor()).thenReturn(user2);
        when(post2.getPostedDate()).thenReturn(LocalDateTime.of(2010, 9, 23, 18, 0, 0));
        when(post2.getHashtags()).thenReturn(new HashSet<>(Arrays.asList("#hoping", "#sleep", "#save")));
        when(post2.getNumOfLikes()).thenReturn(50);

        Comment comment1 = mock(Comment.class);
        when(comment1.getAuthor()).thenReturn(user);
        when(comment1.getAuthor().getEmail()).thenReturn("mark@gmail.com");
        Comment comment2 = mock(Comment.class);
        when(comment2.getAuthor()).thenReturn(user);
        when(comment2.getAuthor().getEmail()).thenReturn("sky@skygram.com");
        when(post2.getComments()).thenReturn(Arrays.asList(comment1,comment2));

        posts.add(post2);
        verify(posts).add(post2);


    }

    @Before
    public void setUp() throws Exception {
        // comment
        currentUser = mock(UserPrincipal.class);
        when(currentUser.getId()).thenReturn(AUTHOR_ID);
        author = new User(AUTHOR_ID);
        author.setUsername("skyteam");
        post = new Post();
        post.setId(POST_ID);
        post.setAuthor(author);
        post.setTitle("Hello SkyGram");

        cmts = new ArrayList<Comment>();
        Comment a = new Comment();
        Comment b = new Comment();
        Comment c = new Comment();
        a.setId(new ObjectId(COMMENT_ID));
        a.setAuthor(author);
        a.setText("huhuhu");
        cmts.add(a);
        cmts.add(b);
        cmts.add(c);
    }

    @Test
    public void testMostLikedKPosts() {
        Long k = 10L;
        List<Post> mostLikedKPosts = MOST_LIKED_K_POSTS.apply(posts, k);
        assertTrue(mostLikedKPosts.size() == k);

        posts.get(1).likedBy("1");
        mostLikedKPosts = MOST_LIKED_K_POSTS.apply(posts, k);

        assertTrue(mostLikedKPosts.contains(posts.get(1)));

        posts.get(2).likedBy("3");
        posts.get(2).likedBy("4");
        mostLikedKPosts = MOST_LIKED_K_POSTS.apply(posts, k);

        assertTrue(mostLikedKPosts.contains(posts.get(1)));
        assertTrue(mostLikedKPosts.contains(posts.get(2)));
        assertTrue(mostLikedKPosts.get(0).equals(posts.get(2)));
    }

    @Test
    public void testTopKCommentsByLengthForUserOnDate() {
        LocalDate testDate = LocalDate.of(2019, 7, 24);
        User testUser = new User("1");
        List<Comment> testComments = new ArrayList<>();

        for (int i = 1; i < 11; i++) {
            Comment comment = new Comment();
            comment.setAuthor(testUser);
            comment.setText("Bla bla bla " + i);
            comment.setCreatedDate(testDate.atTime(19, 30, 54));
            testComments.add(comment);
        }

        posts.get(1).setComments(testComments.subList(0, 4));
        posts.get(2).setComments(testComments.subList(5, 10));

        Long k = 1L;
        List<Comment> topKCommentsByLengthForUserOnDate = TOP_K_COMMENTS_BY_LENGTH_FOR_USER_ON_DATE.apply(testUser, posts, testDate, k);

        assertTrue(topKCommentsByLengthForUserOnDate.size() == k);
        assertTrue(topKCommentsByLengthForUserOnDate.get(0).equals(testComments.get(9)));
    }

    @Test
    public void test_TIMELINE_POSTS() throws Exception {
        List<Post> ps = PostFunctional.TIMELINE_POSTS.apply(posts, user);
        assertEquals(2, ps.size());
    }

    @Test
    public void get_POST_BY_HASHTAG(){
        List<Post> returned = PostFunctional.GET_POST_BY_HASHTAG.apply(posts,"#hoping");
        assertNotNull("Result should not be null", returned);
        assertEquals("Expected Size should be two",2,returned.size());
    }

    @Test
    public void get_MOST_COMMENTED_POSTS(){
        posts.forEach(post -> post.getComments().add(mock(Comment.class)));
        List<Post> returned = PostFunctional.GET_MOST_COMMENTED_POSTS.apply(posts, (long) 1);
        assertNotNull("Result should not be null", returned);
        assertNotEquals("Result should not be zero if there are comments",0,returned.size());
        assertEquals("Expected Size should be One",1,returned.size());
    }

    @Test
    public void get_MOST_LIKED_POSTS_HAVING_COMMENTS_FROM_EMAIL(){
        List<Post> returned = PostFunctional.GET_MOST_LIKED_POSTS_HAVING_COMMENTS_FROM_EMAIL
                .apply(posts, (long) 1, (long)1, "sky");
        assertNotNull("Result should not be null", returned);
        assertNotEquals("Result should not be zero if there are comments",0,returned.size());
        assertEquals("Expected Size should be One",1,returned.size());
    }

    @Test
    public void testUPDATE_COMMENT() throws Exception{
        assert(PostFunctional.UPDATE_COMMENT.apply(cmts, COMMENT_ID, AUTHOR_ID, "test 1") );
    }
}
