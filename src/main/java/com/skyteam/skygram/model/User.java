package com.skyteam.skygram.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public User() {
        this.followers = new HashSet<>();
        this.followings = new HashSet<>();
    }

//    public User(String id, String username, String firstName, String lastName,
//                String email, String phone, String password, LocalDate birthday,
//                LocalDateTime signupDate, List<String> roles, List<String> followers) {
//        this.id = id;
//        this.username = username;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.phone = phone;
//        this.password = password;
//        this.birthday = birthday;
//        this.signupDate = signupDate;
//        this.roles = roles;
//        this.followers
//    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Set<String> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<String> followers) {
        this.followers = followers;
    }

    public Set<String> getFollowings() {
        return followings;
    }

    public void setFollowings(Set<String> followings) {
        this.followings = followings;
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
}
