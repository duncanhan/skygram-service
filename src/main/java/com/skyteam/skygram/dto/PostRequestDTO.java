package com.skyteam.skygram.dto;

public class PostRequestDTO {

  private String title;

  private String[] location;

  private String[] hashtags;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String[] getLocation() {
    return location;
  }

  public void setLocation(String[] location) {
    this.location = location;
  }

  public String[] getHashtags() {
    return hashtags;
  }

  public void setHashtags(String[] hashtags) {
    this.hashtags = hashtags;
  }
}
