package com.skyteam.skygram.service;

import com.skyteam.skygram.dto.CommentDTO;
import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.dto.PostDTO;
import com.skyteam.skygram.dto.PostRequestDTO;
import com.skyteam.skygram.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    Page<PostDTO> getPostsByUser(String username);

    String createPost(String user, String title, MultipartFile[] files, String[] localtion, List<String> hashtags);

    void updatePost(UserPrincipal currentUser, String postId, PostRequestDTO postRequestDTO);

    void deletePost(UserPrincipal currentUser, String postId);

    CommentDTO createComment(UserPrincipal currentUser, String postId, CommentRequestDTO commentRequestDTO);

    void updateComment(UserPrincipal currentUser, String postId, String commentId, CommentRequestDTO commentRequestDTO);

    void deleteComment(UserPrincipal currentUser, String postId, String commentId);

    void like(UserPrincipal currentUser, String postId);

    void unlike(UserPrincipal currentUser, String postId);
}
