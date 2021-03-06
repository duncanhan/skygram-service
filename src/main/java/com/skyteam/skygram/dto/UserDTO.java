package com.skyteam.skygram.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime signupDate;

    public UserDTO() {
    }

    public UserDTO(String id, String username, String firstName, String lastName, String email, String phone, LocalDate birthday, LocalDateTime signupDate) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.signupDate = signupDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDateTime getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(LocalDateTime signupDate) {
        this.signupDate = signupDate;
    }
}
