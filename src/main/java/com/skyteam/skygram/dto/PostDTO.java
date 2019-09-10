package com.skyteam.skygram.dto;

import com.skyteam.skygram.model.Comment;
import com.skyteam.skygram.model.Media;
import java.time.LocalDateTime;
import java.util.List;

public class PostDTO {

  private String id;
  private String title;
  private LocalDateTime date;
  private String location;
  private List<Comment> comments;
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
}
