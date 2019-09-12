package com.skyteam.skygram.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skyteam.skygram.model.Location;
import com.skyteam.skygram.model.Media;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class PostDTO {

    private String id;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("posted_date")
    private LocalDateTime postedDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("last_modified_date")
    private LocalDateTime lastModifiedDate;

    private Location location;

    private List<CommentDTO> comments;

    private Set<String> likes;

    private List<Media> medias;

    private String[] hashtags;

    public PostDTO() {
    }

    public PostDTO(String id, String title, LocalDateTime postedDate, LocalDateTime lastModifiedDate, Location location, List<CommentDTO> comments, Set<String> likes, List<Media> medias, String[] hashtags) {
        this.id = id;
        this.title = title;
        this.postedDate = postedDate;
        this.lastModifiedDate = lastModifiedDate;
        this.location = location;
        this.comments = comments;
        this.likes = likes;
        this.medias = medias;
        this.hashtags = hashtags;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public Set<String> getLikes() {
        return likes;
    }

    public void setLikes(Set<String> likes) {
        this.likes = likes;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public String[] getHashtags() {
        return hashtags;
    }

    public void setHashtags(String[] hashtags) {
        this.hashtags = hashtags;
    }
}
