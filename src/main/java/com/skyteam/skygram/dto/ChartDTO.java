package com.skyteam.skygram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChartDTO {

    private LocalDate date;

    @JsonProperty("num_of_registration")
    private long numOfRegistration;

    @JsonProperty("num_of_posts")
    private long numOfPosts;
}
