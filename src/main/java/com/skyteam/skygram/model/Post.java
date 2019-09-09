package com.skyteam.skygram.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Post {
  private String id;
  private String title;
  private LocalDateTime date;
  private String location;
  private List<Comment> comments;
  private List<String> likes;
  private List<Media> media;

  public Post(String id, String title, LocalDateTime date, String location,
      List<Comment> comments, List<String> likes,
      List<Media> media) {
    this.id = id;
    this.title = title;
    this.date = date;
    this.location = location;
    this.comments = new ArrayList<>();
    this.likes = new ArrayList<>();
    this.media = new ArrayList<>();
  }

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
}
