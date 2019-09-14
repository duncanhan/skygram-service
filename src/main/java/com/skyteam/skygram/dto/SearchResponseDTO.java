package com.skyteam.skygram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDTO {

    private String id;

    private String url;

    private String title;

    private String description;
}
