package com.skyteam.skygram.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Document
public class Comment {

    @Field(value = "_id")
    private ObjectId id;

    @NotNull
    @Field(value = "text")
    private String text;

    @NotNull
    @Field(value = "created_date")
    private LocalDateTime createdDate;

    @NotNull
    @Field(value = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Field(value = "author")
    private String author;

    @Field(value = "likes")
    private Set<String> likes;

    public Comment() {
    }

    public Comment(ObjectId id, @NotNull String text, @NotNull LocalDateTime createdDate, @NotNull LocalDateTime lastModifiedDate, String author, Set<String> likes) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.author = author;
        this.likes = likes;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Set<String> getLikes() {
        return likes;
    }

    public void setLikes(Set<String> likes) {
        this.likes = likes;
    }

    public int getNumOfLikes() {
        return this.likes != null ? this.likes.size() : 0;
    }
}
