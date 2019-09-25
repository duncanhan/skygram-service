package com.skyteam.skygram.functional;

import com.skyteam.skygram.model.Comment;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class PostFunctionalTest {

    private List<Post> posts = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    private User author;
    private static final String POST_ID = "5d7d6c9c37eae66dda9e307d";
    private static final String AUTHOR_ID = "author";
    private static final String COMMENT_ID = "5d7e4b3224aa9a0001f18bb";

    @Before
    public void setUp() throws Exception {
        author = new User(AUTHOR_ID);
        author.setUsername("skyteam");
        author.setEmail("skyteam@gmail.com");
        User author2 = new User(AUTHOR_ID + 2);
        author2.setUsername("skyteam2");
        Post post1 = new Post();
        post1.setId(POST_ID);
        post1.setAuthor(author);
        post1.setTitle("Hello SkyGram");
        post1.setHashtags(new HashSet<>(Arrays.asList("#skygram", "#mpp")));
        post1.setPostedDate(LocalDateTime.of(2019, 9, 23, 1, 0, 0));

        Post post2 = new Post();
        post2.setId(POST_ID + 2);
        post2.setAuthor(author);
        post2.setTitle("Hello SkyGram2");
        post2.setHashtags(new HashSet<>(Arrays.asList("#skygram", "#mum")));
        post2.setPostedDate(LocalDateTime.of(2019, 9, 24, 2, 0, 0));

        Post post3 = new Post();
        post3.setId(POST_ID + 3);
        post3.setAuthor(author);
        post3.setTitle("Hello SkyGram 3");
        post3.setHashtags(new HashSet<>(Arrays.asList("#skygram", "#mpp")));
        post3.setPostedDate(LocalDateTime.of(2019, 9, 25, 2, 0, 0));

        Post post4 = new Post();
        post4.setId(POST_ID + 4);
        post4.setAuthor(author2);
        post4.setTitle("Hello SkyGram 4");
        posts.addAll(Arrays.asList(post1, post2, post3, post4));
        post4.setPostedDate(LocalDateTime.of(2019, 9, 19, 3, 0, 0));

        comments = new ArrayList<>();
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        Comment comment3 = new Comment();
        comment1.setId(new ObjectId(COMMENT_ID + 1));
        comment1.setAuthor(author);
        comment1.setText("huhuhu");
        comment2.setId(new ObjectId(COMMENT_ID + 2));
        comment2.setAuthor(author);
        comment2.setText("huhuhu");
        comment3.setId(new ObjectId(COMMENT_ID + 3));
        comment3.setAuthor(author);
        comment3.setText("huhuhu");
        comments.addAll(Arrays.asList(comment1, comment2, comment3));
    }

    @Test
    public void test_PAGING_POSTS() throws Exception {
        List<Post> ps = PostFunctional.PAGING_POSTS.apply(posts, PageRequest.of(0, 10));
        assertEquals(4, ps.size());
        assertEquals(POST_ID, ps.get(0).getId());
        assertEquals(POST_ID + 2, ps.get(1).getId());
        assertEquals(POST_ID + 3, ps.get(2).getId());
        assertEquals(POST_ID + 4, ps.get(3).getId());
    }

    @Test
    public void test_TIMELINE_POSTS() throws Exception {
        List<Post> ps = PostFunctional.TIMELINE_POSTS.apply(posts, author);
        assertEquals(3, ps.size());
        assertEquals(POST_ID + 3, ps.get(0).getId());

        posts.get(0).getAuthor().getFollowings().add(AUTHOR_ID + 2);
        List<Post> ps2 = PostFunctional.TIMELINE_POSTS.apply(posts, author);
        assertEquals(4, ps2.size());
        assertEquals(POST_ID + 3, ps2.get(0).getId());
    }

    @Test
    public void test_NUM_OF_POSTS() throws Exception {
        long numOfPosts = PostFunctional.NUM_OF_POSTS.apply(posts, LocalDate.of(2019, 9, 23));
        assertEquals(1, numOfPosts);
        long numOfPosts2 = PostFunctional.NUM_OF_POSTS.apply(posts, LocalDate.of(2019, 9, 11));
        assertEquals(0, numOfPosts2);
    }

    @Test
    public void test_TOP_K_MOST_LIKED_POSTS() throws Exception {
        posts.get(1).likedBy(AUTHOR_ID);
        posts.get(1).likedBy(AUTHOR_ID + 2);
        posts.get(3).likedBy(AUTHOR_ID + 2);
        List<Post> mostLikedKPosts = PostFunctional.TOP_K_MOST_LIKED_POSTS.apply(posts, 2L);
        assertEquals(2, mostLikedKPosts.size());
        assertEquals(posts.get(1), mostLikedKPosts.get(0));
    }

    @Test
    public void test_TOP_K_COMMENTS_BY_LENGTH_FOR_USER_ON_DATE() throws Exception {
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

        List<Comment> topKCommentsByLengthForUserOnDate = PostFunctional.TOP_K_COMMENTS_BY_LENGTH_FOR_USER_ON_DATE.apply(testUser, posts, testDate, 1L);

        assertEquals(1, topKCommentsByLengthForUserOnDate.size());
        assertEquals(testComments.get(9), topKCommentsByLengthForUserOnDate.get(0));
    }

    @Test
    public void test_TOP_K_TRENDING_HASHTAGS() throws Exception {
        List<String> hashtags = PostFunctional.TOP_K_TRENDING_HASHTAGS.apply(posts, 10);
        assertEquals(3, hashtags.size());
        assertEquals("#skygram", hashtags.get(0));
    }

    @Test
    public void test_TOP_K_HASHTAGS_START_WITH() throws Exception {
        List<String> hashtags = PostFunctional.TOP_K_HASHTAGS_START_WITH.apply(posts, "#sky", 10);
        assertEquals(1, hashtags.size());
        assertEquals("#skygram", hashtags.get(0));
    }

    @Test
    public void get_POSTS_BY_HASHTAG() throws Exception {
        List<Post> returned = PostFunctional.POSTS_BY_HASHTAG.apply(posts, "#skygram");
        assertNotNull("Result should not be null", returned);
        assertEquals("Expected Size should be two", 3, returned.size());
    }

    @Test
    public void get_POSTS_BY_AUTHOR() throws Exception {
        List<Post> returned = PostFunctional.POSTS_BY_AUTHOR.apply(posts, AUTHOR_ID, PageRequest.of(0, 3));
        assertNotNull("Result should not be null", returned);
        assertEquals("Expected Size should be two", 3, returned.size());
    }

    @Test
    public void get_MOST_COMMENTED_POSTS() throws Exception {
        posts.get(0).getComments().addAll(comments);
        List<Post> returned = PostFunctional.GET_MOST_COMMENTED_POSTS.apply(posts, 1L);
        assertNotNull("Result should not be null", returned);
        assertEquals("Expected Size should be One", 1, returned.size());
        assertEquals(posts.get(0), returned.get(0));
    }

    @Test
    public void get_MOST_LIKED_POSTS_HAVING_COMMENTS_FROM_EMAIL() throws Exception {
        posts.get(0).getComments().addAll(comments);
        List<Post> returned = PostFunctional.GET_MOST_LIKED_POSTS_HAVING_COMMENTS_FROM_EMAIL.apply(posts, 1L, 1L, "sky");
        assertNotNull("Result should not be null", returned);
        assertEquals("Expected Size should be One", 1, returned.size());
        assertEquals(posts.get(0), returned.get(0));
    }

    @Test
    public void test_UPDATE_COMMENT() throws Exception {
        List<Comment> new_comments = PostFunctional.UPDATE_COMMENT
                .apply(LocalDateTime.now(),comments, COMMENT_ID+1, AUTHOR_ID, "test 1");
        assert(new_comments.get(0).getText() == "test 1");
    }

    @Test
    public void testDELETE_COMMENT() throws Exception {
        List<Comment> result = PostFunctional.DELETE_COMMENT.apply(comments, COMMENT_ID + 1, AUTHOR_ID);
        assertEquals(2, result.size());

        List<Comment> result2 = PostFunctional.DELETE_COMMENT.apply(comments, COMMENT_ID + 10, AUTHOR_ID);
        assertEquals(3, result2.size());
    }
}
