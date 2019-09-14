package com.skyteam.skygram.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Document(collection = "users")
public class User implements Serializable {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("username")
    private String username;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Indexed(unique = true, sparse = true)
    @Field("email")
    private String email;

    @Indexed(unique = true, sparse = true)
    @Field("phone")
    private String phone;

    @Field("password")
    private String password;

    @Field("birthday")
    private LocalDate birthday;

    @Field("signup_date")
    private LocalDateTime signupDate;

    @Field("roles")
    private List<String> roles;

    @Field("followers")
    private Set<String> followers;

    @Field("followings")
    private Set<String> followings;

    @Field("profile_picture")
    private String profilePicture;

    public User() {
        this.roles = new ArrayList<>();
        this.followers = new HashSet<>();
        this.followings = new HashSet<>();
    }

    public void follow(String userId) {
        this.followings.add(userId);
    }

    public void followedBy(String userId) {
        this.followers.add(userId);
    }

    public void unfollow(String userId) {
        this.followings.remove(userId);
    }

    public int getNumOfFollowers() {
        return this.followers != null ? this.followers.size() : 0;
    }

    public int getNumOfFollowings() {
        return this.followings != null ? this.followings.size() : 0;
    }
}
