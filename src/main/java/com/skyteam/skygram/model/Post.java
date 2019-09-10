package com.skyteam.skygram.model;

import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="posts")
public class Post {

  public Post(String id, String username, String title, LocalDateTime date, String location, List<Media> media, String[] hashtags) {
    this.id = id;
    this.title = title;
    this.date = date;
    this.location = location;
    this.media = media;
    this.username = username;
    this.hashtags = hashtags;
  }

  @Id
  private String id;

  @Field
  @NonNull
  private String username;

  @Field
  private String title;

  @Field
  private LocalDateTime date;

  @Field
  private String location;

  @Field
  @Nullable
  private String[] hashtags;

  @Field
  @Nullable
  private List<Comment> comments;

  @Nullable
  private List<String> likes;

  private List<Media> media;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
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

  public List<Media> getMedia() {
    return media;
  }

  public void setMedia(List<Media> media) {
    this.media = media;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String[] getHashtags() {
    return hashtags;
  }

  public void setHashtags(String[] hashtags) {
    this.hashtags = hashtags;
  }
}
