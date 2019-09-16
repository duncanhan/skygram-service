package com.skyteam.skygram.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@Document
public abstract class Media {

    private String id;

    private String url;

    @Field(value = "file_format")
    private String fileFormat;

    private String type;
}
