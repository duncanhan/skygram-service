package com.skyteam.skygram.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentRequestDTO {

    @NotBlank(message = "Comment text is required")
    private String text;
}
