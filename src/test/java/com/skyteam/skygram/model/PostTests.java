package com.skyteam.skygram.model;

import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.enumerable.FileType;
import com.skyteam.skygram.exception.NoPermissionException;
import com.skyteam.skygram.exception.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PostTests {

    private Post post;

    private User author;

    private Comment comment;

    private Media media;

    @Before
    public void setUp() throws Exception {
        post = new Post();
        post.setId("5d7d6de937eae66e4c033283");
        post.setTitle("title");
        post.setPostedDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        post.setLastModifiedDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        post.setHashtags(new HashSet<>(Collections.singleton("#skyteam")));
        post.setLocation(new Location());
        author = mock(User.class);
        when(author.getId()).thenReturn("author");
        when(author.getUsername()).thenReturn("skyteam");
        post.setAuthor(author);
        comment = new Comment();
        comment.setId(new ObjectId("5d7d6de937eae66e4c033283"));
        comment.setText("comment");
        comment.setAuthor(author);
        media = new Photo("5d7d6de937eae66e4c033283_1", "image_url", "image", FileType.IMAGE.getValue());
    }

    @Test
    public void testNewPost() throws Exception {
        assertNotNull(post);
        assertEquals("5d7d6de937eae66e4c033283", post.getId());
        assertEquals("title", post.getTitle());
        assertEquals(LocalDateTime.of(2019, 1, 1, 0, 0, 0), post.getPostedDate());
        assertEquals(LocalDateTime.of(2019, 1, 1, 0, 0, 0), post.getLastModifiedDate());
        assertThat(post.getHashtags(), IsCollectionContaining.hasItem("#skyteam"));
        assertNotNull(post.getLocation());
        assertEquals(author, post.getAuthor());
    }

    @Test
    public void testAddComment() throws Exception {
        post.addComment(comment);
        assertThat(post.getComments(), IsCollectionContaining.hasItem(comment));
    }

    @Test
    public void testUpdateComment_success() throws Exception {
        post.addComment(comment);
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("updated comment");
        post.updateComment("5d7d6de937eae66e4c033283", "author", commentRequestDTO);
        assertEquals("updated comment", post.getComments().get(0).getText());
    }

    @Test(expected = NoPermissionException.class)
    public void testUpdateComment_noPermission() throws Exception {
        post.addComment(comment);
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("updated comment");
        post.updateComment("5d7d6de937eae66e4c033283", "author0", commentRequestDTO);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateComment_commentNotFound() throws Exception {
        post.addComment(comment);
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("updated comment");
        post.updateComment("5d7d6de937eae66e4c033283_1", "author", commentRequestDTO);
    }

    @Test
    public void testDeleteComment_success() throws Exception {
        post.addComment(comment);
        assertEquals(1, post.getComments().size());
        post.deleteComment("5d7d6de937eae66e4c033283", "author");
        assertEquals(0, post.getComments().size());
    }

    @Test(expected = NoPermissionException.class)
    public void testDeleteComment_noPermission() throws Exception {
        post.addComment(comment);
        post.deleteComment("5d7d6de937eae66e4c033283", "author0");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteComment_commentNotFound() throws Exception {
        post.addComment(comment);
        post.deleteComment("5d7d6de937eae66e4c033283_1", "author");
    }

    @Test
    public void testLike() throws Exception {
        post.likedBy("user1");
        assertThat(post.getLikes(), not(IsEmptyCollection.empty()));
        assertThat(post.getLikes(), IsCollectionContaining.hasItem("user1"));
    }

    @Test
    public void testUnlike() throws Exception {
        post.likedBy("user1");
        assertThat(post.getLikes(), not(IsEmptyCollection.empty()));
        post.unlikeBy("user1");
        assertThat(post.getLikes(), IsEmptyCollection.empty());
    }

    @Test
    public void testGetNumOfLikes() throws Exception {
        post.likedBy("user1");
        assertEquals(1, post.getNumOfLikes());
        assertThat(post.getLikes(), IsCollectionContaining.hasItem("user1"));
    }

    @Test
    public void testNew() throws Exception {
        Post post = new Post("user1", "title", new HashSet<>(), null);
        assertEquals("title", post.getTitle());
    }

    @Test
    public void testAddMedia() throws Exception {
        post.addMedia(media);
        assertEquals(media, post.getMedia().get(0));
    }

    @Test
    public void testUpdateMedia() throws Exception {
        post.addMedia(media);
        post.updateMedia(new Video("id", "url", "mp4", FileType.VIDEO.getValue(), 100));
        assertEquals("url", post.getMedia().get(0).getUrl());
    }
}
