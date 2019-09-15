package com.skyteam.skygram.model;

import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.exception.NoPermissionException;
import com.skyteam.skygram.exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostTests {

    private Post post;

    private User author;

    private Comment comment;

    private Media media;

    @Before
    public void setUp() throws Exception {
        post = new Post();
        post.setTitle("title");
        author = mock(User.class);
        when(author.getId()).thenReturn("author");
        when(author.getUsername()).thenReturn("skyteam");
        post.setAuthor(author);
        comment = new Comment();
        comment.setId(new ObjectId("5d7d6de937eae66e4c033283"));
        comment.setText("comment");
        comment.setAuthor(author);
    }

    @Test
    public void testNewPost() {
        assertNotNull(post);
        assertEquals("title", post.getTitle());
    }

    @Test
    public void testAddComment() {
        post.addComment(comment);
        assertThat(post.getComments(), IsCollectionContaining.hasItem(comment));
    }

    @Test
    public void testUpdateComment_success() {
        post.addComment(comment);
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("updated comment");
        post.updateComment("5d7d6de937eae66e4c033283", "author", commentRequestDTO);
        assertEquals("updated comment", post.getComments().get(0).getText());
    }

    @Test(expected = NoPermissionException.class)
    public void testUpdateComment_noPermission() {
        post.addComment(comment);
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("updated comment");
        post.updateComment("5d7d6de937eae66e4c033283", "author0", commentRequestDTO);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateComment_commentNotFound() {
        post.addComment(comment);
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("updated comment");
        post.updateComment("5d7d6de937eae66e4c033283_1", "author", commentRequestDTO);
    }

    @Test
    public void testDeleteComment_success() {
        post.addComment(comment);
        assertEquals(1, post.getComments().size());
        post.deleteComment("5d7d6de937eae66e4c033283", "author");
        assertEquals(0, post.getComments().size());
    }

    @Test(expected = NoPermissionException.class)
    public void testDeleteComment_noPermission() {
        post.addComment(comment);
        post.deleteComment("5d7d6de937eae66e4c033283", "author0");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteComment_commentNotFound() {
        post.addComment(comment);
        post.deleteComment("5d7d6de937eae66e4c033283_1", "author");
    }

    @Test
    public void testLike() {
        post.likedBy("user1");
        assertThat(post.getLikes(), not(IsEmptyCollection.empty()));
        assertThat(post.getLikes(), IsCollectionContaining.hasItem("user1"));
    }

    @Test
    public void testUnlike() {
        post.likedBy("user1");
        assertThat(post.getLikes(), not(IsEmptyCollection.empty()));
        post.unlikeBy("user1");
        assertThat(post.getLikes(), IsEmptyCollection.empty());
    }
}
