package com.skyteam.skygram.model;

import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;

import java.util.*;

import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.exception.NoPermissionException;
import com.skyteam.skygram.exception.ResourceNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "posts")
@TypeAlias("Post")
public class Post {

    @Id
    private String id;

    @NonNull
    @DBRef
    @Field(value = "author")
    private User author;

    @Field(value = "title")
    private String title;

    @NotNull
    @Field(value = "posted_date")
    private LocalDateTime postedDate;

    @Field(value = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Field(value = "hashtags")
    private Set<String> hashtags = new HashSet<>();

    @Field(value = "location")
    private Location location;

    @Field(value = "media")
    private List<Media> media = new ArrayList<>();

    @Nullable
    @Field(value = "comments")
    private List<Comment> comments = new ArrayList<>();

    @Nullable
    @Field(value = "likes")
    private Set<String> likes = new HashSet<>();

    public Post(String author, String title, Set<String> hashtags, Location location) {
        this.author = new User(author);
        this.title = title;
        this.postedDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.hashtags = hashtags;
        this.location = location;
    }

    public void addComment(Comment comment) {
        if (CollectionUtils.isEmpty(this.comments)) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }

    public void updateComment(String commentId, String userId, CommentRequestDTO commentRequestDTO) {
        if (CollectionUtils.isEmpty(this.comments)) return;
        for (Comment comment : this.comments) {
            if (comment.getId().equals(commentId)) {
                if (!comment.getAuthor().equals(userId)) {
                    throw new NoPermissionException();
                }
                comment.setText(commentRequestDTO.getText());
                comment.setLastModifiedDate(LocalDateTime.now());
            }
        }
    }

    public void deleteComment(String commentId, String userId) {
        if (CollectionUtils.isEmpty(this.comments)) return;
        for (Comment comment : this.comments) {
            if (comment.getId() != null && comment.getId().toString().equals(commentId)) {
                if (!comment.getAuthor().equals(userId)) {
                    throw new NoPermissionException();
                }
                this.comments.remove(comment);
                return;
            }
        }
        throw new ResourceNotFoundException("Comment", "id", commentId);
    }

    public void likedBy(String userId) {
        if (this.likes == null) {
            this.likes = new HashSet<>();
        }
        this.likes.add(userId);
    }

    public void unlikeBy(String userId) {
        if (this.likes == null) {
            this.likes = new HashSet<>();
        }
        this.likes.remove(userId);
    }

    public void addMedia(Media media) {
        if (CollectionUtils.isEmpty(this.media)) {
            this.media = new ArrayList<>();
        }
        this.media.add(media);
    }

    public void updateMedia(Media media) {
        if (CollectionUtils.isEmpty(this.media)) {
            this.media = new ArrayList<>();
        }
        this.media.clear();
        this.media.add(media);
    }

    public int getNumOfLikes() {
        return this.likes != null ? this.likes.size() : 0;
    }
}
