package com.skyteam.skygram.model;

import java.util.ArrayList;

public class Comment {

    private int commentId;
    private int userId;
    private int postId;
    private String comment;
    private ArrayList<User> likes;

    public Comment(int userId, int postId,String comment){
        this.userId = userId;
        this.postId = postId;
        this.comment = comment;
        likes = new ArrayList<User>();
    }

    public boolean like(User u){
        likes.add(u);
        return true;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
