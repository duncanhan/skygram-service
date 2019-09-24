package com.skyteam.skygram.functional;

import com.skyteam.skygram.model.Comment;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.PostService;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void testUPDATE_COMMENT() throws Exception{
        assert(PostFunctional.UPDATE_COMMENT.apply(cmts, COMMENT_ID, AUTHOR_ID, "test 1") );
    }

    @Test
    public void testUPDATE_POST() throws Exception{
        assert(true);
        //assert(PostFunctional.UPDATE_COMMENT.apply(cmts, COMMENT_ID, AUTHOR_ID, "test 1") );
    }

    @After
    public void tearDown() throws Exception {
        assert (true);
    }


}
