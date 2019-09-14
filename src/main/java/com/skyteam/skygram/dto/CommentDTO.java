package com.skyteam.skygram.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {

    private String id;

    private String text;

    private LocalDateTime date;

    private String author;

    private int numOfLikes;
}
