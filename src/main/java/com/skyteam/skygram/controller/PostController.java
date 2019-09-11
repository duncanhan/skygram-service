package com.skyteam.skygram.controller;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.dto.CommentDTO;
import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.dto.PostRequestDTO;
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
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @ApiOperation(value = "Post", authorizations = {@Authorization(value = "apiKey")})
    @RequestMapping(value = {"/"}, method = {RequestMethod.POST}, consumes = {"multipart/form-data"})
    public Response post(@RequestParam("files") MultipartFile file, @RequestParam("user") String user,
                         @RequestParam("title") String title,
                         @RequestParam("localtion") String[] localtion,
                         @RequestParam("hashtags") String[] hashtags) {
        MultipartFile[] files = {file};
        return ResponseBuilder.buildSuccess(postService.createPost(user, title, files, localtion, Arrays.asList(hashtags)));
    }

    @ApiOperation(value = "Post", authorizations = {@Authorization(value = "apiKey")})
    @RequestMapping(value = {"/post"}, method = {RequestMethod.POST}, consumes = {"multipart/form-data"})
    public Response post(@ApiIgnore @CurrentUser UserPrincipal currentUser, @RequestParam("files") MultipartFile file,
                         @RequestParam("title") String title,
                         @RequestParam("localtion") String[] localtion,
                         @RequestParam("hashtags") List<String> hashtags) {
        MultipartFile[] files = {file};
        return ResponseBuilder.buildSuccess(postService.createPost(currentUser.getId(), title, files, localtion, hashtags));
    }

    @ApiOperation(value = "Update post", authorizations = {@Authorization(value = "apiKey")})
    @PutMapping(value = {"/{id}"}, consumes = {"multipart/form-data"})
    public Response updatePost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                               @PathVariable("id") String postId,
                               @ModelAttribute PostRequestDTO postRequestDTO) {
        postService.updatePost(currentUser, postId, postRequestDTO);
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
    @PostMapping("/{postId}/comments/{commentId}")
    public Response commentPost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                @PathVariable("postId") String postId,
                                @PathVariable("commentId") String commentId,
                                @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        postService.updateComment(currentUser, postId, commentId, commentRequestDTO);
        return ResponseBuilder.buildSuccess();
    }

    @ApiOperation(value = "Delete a comment", authorizations = {@Authorization(value = "apiKey")})
    @DeleteMapping("/{postId}/comments/{commentId}")
    public Response deleteComment(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                  @PathVariable("postId") String postId,
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
