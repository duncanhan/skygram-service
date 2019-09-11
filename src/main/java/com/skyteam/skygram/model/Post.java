package com.skyteam.skygram.model;

import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;

import java.util.*;

import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.exception.ResourceNotFoundException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    @NonNull
    @Field(value = "author")
    private String author;

    @Field(value = "title")
    private String title;

    @NotNull
    @Field(value = "posted_date")
    private LocalDateTime postedDate;

    @Field(value = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Field(value = "hashtags")
    private Set<String> hashtags;

    @Field(value = "location")
    private Location location;

    @Field(value = "media")
    private List<Media> medias;

    @Nullable
    @Field(value = "comments")
    private List<Comment> comments;

    @Nullable
    @Field(value = "likes")
    private Set<String> likes;

    public Post(String author, String title, Set<String> hashtags, Location location) {
        this.author = author;
        this.title = title;
        this.postedDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.hashtags = hashtags;
        this.location = location;
        this.medias = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.likes = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<String> hashtags) {
        this.hashtags = hashtags;
    }

    public void setHashtags(String[] hashtags) {
        this.hashtags = new HashSet<>(Arrays.asList(hashtags));
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<String> getLikes() {
        return likes;
    }

    public void setLikes(Set<String> likes) {
        this.likes = likes;
    }

    public void addComment(Comment comment) {
        if (CollectionUtils.isEmpty(this.comments)) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }

    public void updateComment(String commentId, CommentRequestDTO commentRequestDTO) {
        if (CollectionUtils.isEmpty(this.comments)) return;
        for (Comment comment : this.comments) {
            if (comment.getId().equals(comment)) {
                comment.setText(commentRequestDTO.getText());
                comment.setLastModifiedDate(LocalDateTime.now());
            }
        }
    }

    public void deleteComment(String commentId) throws ResourceNotFoundException {
        if (CollectionUtils.isEmpty(this.comments)) return;
        for (Comment comment : this.comments) {
            if (comment.getId() != null && comment.getId().toString().equals(commentId)) {
                this.comments.remove(comment);
                return;
            }
        }
        throw new ResourceNotFoundException("Comment", "id", commentId);
    }

    public void likedBy(String userId) {
        if (this.likes == null) {
            this.likes = new HashSet<>();
        }
        this.likes.add(userId);
    }

    public void unlikeBy(String userId) {
        if (this.likes == null) {
            this.likes = new HashSet<>();
        }
        this.likes.remove(userId);
    }
}
