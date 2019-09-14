package com.skyteam.skygram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "First Name is required")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @JsonProperty("last_name")
    private String lastName;

    @Email(message = "Wrong email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    private LocalDate birthday;

    @NotBlank(message = "Password is required")
    private String password;
}
