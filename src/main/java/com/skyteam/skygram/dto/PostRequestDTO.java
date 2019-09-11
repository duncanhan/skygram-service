package com.skyteam.skygram.dto;

import org.springframework.web.multipart.MultipartFile;

public class PostRequestDTO {

  private String title;

  private MultipartFile file;

  private String[] location;

  private String[] hashtags;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public MultipartFile getFile() {
    return file;
  }

  public void setFile(MultipartFile file) {
    this.file = file;
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
