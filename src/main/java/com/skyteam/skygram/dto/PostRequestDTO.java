package com.skyteam.skygram.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDTO {

  private String title;

  private String[] location;

  private String[] hashtags;
}
