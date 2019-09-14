package com.skyteam.skygram.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
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

    public int getNumOfLikes() {
        return this.likes != null ? this.likes.size() : 0;
    }
}
