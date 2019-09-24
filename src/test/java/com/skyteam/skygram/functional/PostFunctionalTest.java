package com.skyteam.skygram.functional;

import com.skyteam.skygram.model.Comment;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.skyteam.skygram.functional.PostFunctional.MOST_LIKED_K_POSTS;
import static com.skyteam.skygram.functional.PostFunctional.TOP_K_COMMENTS_BY_LENGTH_FOR_USER_ON_DATE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostFunctionalTest {

    private static List<Post> posts;

    private static User user;

    @BeforeClass
    public static void beforeClass() throws Exception {
        posts = spy(new ArrayList<>());

        user = mock(User.class);
        when(user.getFollowings()).thenReturn(new HashSet<>(Arrays.asList("user2")));
        Post post1 = mock(Post.class);
        when(post1.getAuthor()).thenReturn(user);
        when(post1.getPostedDate()).thenReturn(LocalDateTime.of(2010, 9, 23, 10, 0, 0));
        posts.add(post1);
        verify(posts).add(post1);

        User user2 = mock(User.class);
        when(user2.getId()).thenReturn("user2");
        when(user2.getFollowings()).thenReturn(new HashSet<>(Arrays.asList("user")));
        Post post2 = mock(Post.class);
        when(post2.getAuthor()).thenReturn(user2);
        when(post2.getPostedDate()).thenReturn(LocalDateTime.of(2010, 9, 23, 18, 0, 0));
        posts.add(post2);
        verify(posts).add(post2);
    }

    @Before
    public void setUp() throws Exception {
//        for (int i = 1; i < 11; i++) {
//            Post post = new Post();
//            LocalDateTime testDate = LocalDateTime.of(2019, 7, i, 19, i, 21);
//            post.setPostedDate(testDate);
//            posts.add(post);
//        }
    }

    @After
    public void tearDown() throws Exception {
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
}
