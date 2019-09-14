package com.skyteam.skygram.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skyteam.skygram.model.Location;
import com.skyteam.skygram.model.Media;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {

    private String id;

    private String title;

    private AuthorDTO author;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("posted_date")
    private LocalDateTime postedDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("last_modified_date")
    private LocalDateTime lastModifiedDate;

    private Location location;

    private List<CommentDTO> comments;

    @JsonProperty("num_of_likes")
    private int numOfLikes;

    private List<Media> media;

    private String[] hashtags;

    @JsonProperty("is_liked")
    private boolean isLiked;

    @JsonProperty("is_owned")
    private boolean isOwned;
}
