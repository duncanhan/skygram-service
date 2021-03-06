package com.skyteam.skygram.service;

import com.skyteam.skygram.dto.*;
import com.skyteam.skygram.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostService {

    Page<PostDTO> getPostsByUser(String username,Pageable pageable);

    PostDTO createPost(UserPrincipal currentUser, String title, MultipartFile[] files, String[] location, String[] hashtags) throws IOException;

    void updatePost(UserPrincipal currentUser, String postId, String title, MultipartFile[] files, String[] location, String[] hashtags) throws IOException;

    void deletePost(UserPrincipal currentUser, String postId);

    CommentDTO createComment(UserPrincipal currentUser, String postId, CommentRequestDTO commentRequestDTO);

    void updateComment(UserPrincipal currentUser, String postId, String commentId, CommentRequestDTO commentRequestDTO);

    void deleteComment(UserPrincipal currentUser, String postId, String commentId);

    void like(UserPrincipal currentUser, String postId);

    void unlike(UserPrincipal currentUser, String postId);

    Page<SearchResponseDTO> searchHashtags(String q, Pageable page);
}
