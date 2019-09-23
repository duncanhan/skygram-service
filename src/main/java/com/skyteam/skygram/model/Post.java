package com.skyteam.skygram.model;

import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.exception.NoPermissionException;
import com.skyteam.skygram.exception.ResourceNotFoundException;
import com.skyteam.skygram.functional.PostFunctional;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

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
        String newComment = commentRequestDTO.getText();

        boolean res = PostFunctional.UPDATE_COMMENT.apply(this.comments, commentId, userId, newComment);
        if(!res){
            throw new ResourceNotFoundException("Comment", "id", commentId);
        }
        /*for (Comment comment : this.comments) {
            if (comment.getId().toString().equals(commentId)) {
                if (!comment.getAuthor().getId().equals(userId)) {
                    throw new NoPermissionException();
                }
                comment.setText(commentRequestDTO.getText());
                comment.setLastModifiedDate(LocalDateTime.now());
                return;
            }
        }
        throw new ResourceNotFoundException("Comment", "id", commentId);
        */
    }

    public void deleteComment(String commentId, String userId) {
        if (CollectionUtils.isEmpty(this.comments)) return;
        for (Comment comment : this.comments) {
            if (comment.getId() != null && comment.getId().toString().equals(commentId)) {
                if (!comment.getAuthor().getId().equals(userId)) {
                    throw new NoPermissionException();
                }
                this.comments.remove(comment);
                return;
            }
        }
        throw new ResourceNotFoundException("Comment", "id", commentId);
    }

    public void likedBy(String userId) {
        this.likes.add(userId);
    }

    public void unlikeBy(String userId) {
        this.likes.remove(userId);
    }

    public void addMedia(Media media) {
        this.media.add(media);
    }

    public void updateMedia(Media media) {
        this.media.clear();
        this.media.add(media);
    }

    public int getNumOfLikes() {
        return this.likes != null ? this.likes.size() : 0;
    }
}
