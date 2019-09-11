package com.skyteam.skygram.model;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    @NonNull
    @Field(value = "username")
    private String username;

    @Field(value = "title")
    private String title;

    @NotNull
    @Field(value = "posted_date")
    private LocalDateTime postedDate;

    @Field(value = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Field(value = "hashtags")
    private List<String> hashtags;

    @Field(value = "location")
    private String[] location;

    @Field(value = "media")
    private List<String> medias;

    @DBRef(lazy = true)
    private List<Comment> comments;

    @Field(value = "likes")
    private List<String> likes;

    public Post(String id, String username, String title, @NotNull LocalDateTime postedDate, LocalDateTime lastModifiedDate, List<String> hashtags, String[] location, List<String> medias, List<Comment> comments, List<String> likes) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.postedDate = postedDate;
        this.lastModifiedDate = lastModifiedDate;
        this.hashtags = hashtags;
        this.location = location;
        this.medias = medias;
        this.comments = comments;
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String[] getLocation() {
        return location;
    }

    public void setLocation(String[] location) {
        this.location = location;
    }

    public List<String> getMedias() {
        return medias;
    }

    public void setMedias(List<String> medias) {
        this.medias = medias;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }
}
