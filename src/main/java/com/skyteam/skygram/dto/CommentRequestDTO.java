package com.skyteam.skygram.dto;

import javax.validation.constraints.NotBlank;

public class CommentRequestDTO {

    @NotBlank(message = "Comment text is required")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
