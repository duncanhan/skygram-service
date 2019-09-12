package com.skyteam.skygram.controller;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.dto.CommentDTO;
import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.security.CurrentUser;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.PostService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @ApiOperation(value = "Create a post", authorizations = {@Authorization(value = "apiKey")})
    @PostMapping(consumes = "multipart/form-data")
    public Response post(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                         @Valid @RequestPart("media") @NotNull(message = "Please choose at least one photo/video") @NotBlank(message = "Please choose at least one photo/video") MultipartFile file,
                         @RequestPart("title") String title,
                         @RequestParam("location") String[] location,
                         @RequestParam("hashtags") String[] hashtags) throws IOException {
        MultipartFile[] files = {file};
        return ResponseBuilder.buildSuccess(postService.createPost(currentUser, title, files, location, hashtags));
    }

    @ApiOperation(value = "Update post", authorizations = {@Authorization(value = "apiKey")})
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public Response updatePost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                               @PathVariable("id") String postId,
                               @RequestPart("title") String title,
                               @RequestPart("media") MultipartFile file,
                               @RequestParam("location") String[] location,
                               @RequestParam("hashtags") String[] hashtags) throws IOException {
        postService.updatePost(currentUser, postId, title, new MultipartFile[]{file}, location, hashtags);
        return ResponseBuilder.buildSuccess("Post is updated", null);
    }

    @ApiOperation(value = "Delete post", authorizations = {@Authorization(value = "apiKey")})
    @DeleteMapping("/{id}")
    public Response deletePost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                               @PathVariable("id") String postId) {
        postService.deletePost(currentUser, postId);
        return ResponseBuilder.buildSuccess("Post is deleted", null);
    }

    @ApiOperation(value = "Comment on a post", authorizations = {@Authorization(value = "apiKey")})
    @PostMapping("/{id}/comments")
    public Response commentPost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                @PathVariable("id") String postId,
                                @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentDTO commentDTO = postService.createComment(currentUser, postId, commentRequestDTO);
        return ResponseBuilder.buildSuccess(commentDTO);
    }

    @ApiOperation(value = "Update comment", authorizations = {@Authorization(value = "apiKey")})
    @PutMapping("/{id}/comments/{commentId}")
    public Response commentPost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                @PathVariable("id") String postId,
                                @PathVariable("commentId") String commentId,
                                @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        postService.updateComment(currentUser, postId, commentId, commentRequestDTO);
        return ResponseBuilder.buildSuccess();
    }

    @ApiOperation(value = "Delete a comment", authorizations = {@Authorization(value = "apiKey")})
    @DeleteMapping("/{id}/comments/{commentId}")
    public Response deleteComment(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                  @PathVariable("id") String postId,
                                  @PathVariable("commentId") String commentId) {
        postService.deleteComment(currentUser, postId, commentId);
        return ResponseBuilder.buildSuccess();
    }

    @ApiOperation(value = "Like post", authorizations = {@Authorization(value = "apiKey")})
    @PostMapping("/{id}/like")
    public Response likePost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                             @PathVariable("id") String postId) {
        postService.like(currentUser, postId);
        return ResponseBuilder.buildSuccess();
    }

    @ApiOperation(value = "Unlike post", authorizations = {@Authorization(value = "apiKey")})
    @PostMapping("/{id}/unlike")
    public Response unlikePost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                               @PathVariable("id") String postId) {
        postService.unlike(currentUser, postId);
        return ResponseBuilder.buildSuccess();
    }
}
