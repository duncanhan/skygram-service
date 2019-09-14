package com.skyteam.skygram.model;

import com.mongodb.lang.NonNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    @NonNull
    @DBRef
    @Field(value = "author")
    private User author;

    @Field(value = "likes")
    private Set<String> likes = new HashSet<>();

    public int getNumOfLikes() {
        return this.likes != null ? this.likes.size() : 0;
    }
}
