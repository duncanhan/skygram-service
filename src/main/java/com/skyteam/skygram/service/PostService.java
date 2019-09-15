package com.skyteam.skygram.service;

import com.skyteam.skygram.dto.*;
import com.skyteam.skygram.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

public interface PostService {

    Page<PostDTO> getPostsByUser(UserPrincipal currentUser, String username,Pageable pageable);

    PostDTO createPost(UserPrincipal currentUser, String title, MultipartFile[] files, String[] location, String[] hashtags) throws IOException;

    boolean updatePost(UserPrincipal currentUser, String postId, String title, MultipartFile[] files, String[] location, String[] hashtags) throws IOException;

    boolean deletePost(UserPrincipal currentUser, String postId);

    CommentDTO createComment(UserPrincipal currentUser, String postId, CommentRequestDTO commentRequestDTO);

    boolean updateComment(UserPrincipal currentUser, String postId, String commentId, CommentRequestDTO commentRequestDTO);

    boolean deleteComment(UserPrincipal currentUser, String postId, String commentId);

    boolean like(UserPrincipal currentUser, String postId);

    boolean unlike(UserPrincipal currentUser, String postId);

    Page<SearchResponseDTO> searchHashtags(String q, Pageable page);

    Page<PostDTO> getTimelinePosts(UserPrincipal currentUser, Pageable page);

    PostDTO getPostDetail(UserPrincipal currentUser, String postId);

    long getNumOfPosts(LocalDate date);
}
