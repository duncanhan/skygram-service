package com.skyteam.skygram.model;

import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import java.util.ArrayList;
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
    private List<String> hashtags;

    @Field(value = "location")
    private String[] location;

    @Field(value = "media")
    private List<Media> medias;

    @DBRef(lazy = true)
    @Nullable
    private List<Comment> comments;

    @Field(value = "likes")
    @Nullable
    private List<String> likes;

    public Post(String author, String title, List<String> hashtags, String[] location) {
        this.author = author;
        this.title = title;
        this.postedDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.hashtags = hashtags;
        this.location = location;
        this.medias = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.likes = new ArrayList<>();
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

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }
}
