package com.skyteam.skygram.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String id;

    private String username;

    @NotBlank(message = "First Name is required")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @JsonProperty("last_name")
    private String lastName;

    private String email;

    private String phone;

    private LocalDate birthday;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("signup_date")
    private LocalDateTime signupDate;

    @JsonProperty("num_of_followers")
    private int numOfFollowers;

    @JsonProperty("num_of_followings")
    private int numOfFollowings;

    @JsonProperty("is_following")
    private boolean isFollowing;

    private boolean followed;
}
