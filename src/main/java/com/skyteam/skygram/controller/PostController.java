package com.skyteam.skygram.controller;

import com.skyteam.skygram.common.PageDTO;
import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.dto.CommentDTO;
import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.dto.PostDTO;
import com.skyteam.skygram.security.CurrentUser;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.PostService;
import com.skyteam.skygram.util.PageUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "errors")})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response post(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                         @Valid @RequestPart("media") @NotNull(message = "Please choose at least one photo/video") @NotBlank(message = "Please choose at least one photo/video") MultipartFile file,
                         @RequestPart("title") String title,
                         @RequestParam(value = "location", required = false) String[] location,
                         @RequestParam(value = "hashtags", required = false) String[] hashtags) throws IOException {
        MultipartFile[] files = {file};
        return ResponseBuilder.buildSuccess(postService.createPost(currentUser, title, files, location, hashtags));
    }

    @ApiOperation(value = "Update post", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Post not found"),
            @ApiResponse(code = 400, message = "errors")})
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updatePost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                               @PathVariable("id") String postId,
                               @RequestPart("title") String title,
                               @RequestPart("media") MultipartFile file,
                               @RequestParam(value = "location", required = false) String[] location,
                               @RequestParam(value = "hashtags", required = false) String[] hashtags) throws IOException {
        postService.updatePost(currentUser, postId, title, new MultipartFile[]{file}, location, hashtags);
        return ResponseBuilder.buildSuccess("Post is updated", null);
    }

    @ApiOperation(value = "Delete post", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Post not found")})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response deletePost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                               @PathVariable("id") String postId) {
        postService.deletePost(currentUser, postId);
        return ResponseBuilder.buildSuccess("Post is deleted", null);
    }

    @ApiOperation(value = "Comment on a post", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Post not found")})
    @PostMapping(value = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response commentPost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                @PathVariable("id") String postId,
                                @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentDTO commentDTO = postService.createComment(currentUser, postId, commentRequestDTO);
        return ResponseBuilder.buildSuccess(commentDTO);
    }

    @ApiOperation(value = "Update comment", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Post/Comment not found"),
            @ApiResponse(code = 400, message = "errors")})
    @PutMapping(value = "/{id}/comments/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response commentPost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                @PathVariable("id") String postId,
                                @PathVariable("commentId") String commentId,
                                @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        postService.updateComment(currentUser, postId, commentId, commentRequestDTO);
        return ResponseBuilder.buildSuccess();
    }

    @ApiOperation(value = "Delete a comment", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Post/Comment not found"),
            @ApiResponse(code = 400, message = "errors")})
    @DeleteMapping(value = "/{id}/comments/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteComment(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                  @PathVariable("id") String postId,
                                  @PathVariable("commentId") String commentId) {
        postService.deleteComment(currentUser, postId, commentId);
        return ResponseBuilder.buildSuccess("Comment is deleted", null);
    }

    @ApiOperation(value = "Like post", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Post not found")})
    @PostMapping(value = "/{id}/like", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response likePost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                             @PathVariable("id") String postId) {
        postService.like(currentUser, postId);
        return ResponseBuilder.buildSuccess();
    }

    @ApiOperation(value = "Unlike post", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Post not found")})
    @PostMapping(value = "/{id}/unlike", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response unlikePost(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                               @PathVariable("id") String postId) {
        postService.unlike(currentUser, postId);
        return ResponseBuilder.buildSuccess();
    }

    @ApiOperation(value = "Get timeline posts", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "errors")})
    @GetMapping(value = "/timeline", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getTimelinePosts(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                     @ModelAttribute PageDTO pageDTO) {
        Page<PostDTO> posts = postService.getTimelinePosts(currentUser, PageUtil.initPage(pageDTO, new Sort(Sort.Direction.DESC, "postedDate")));
        return ResponseBuilder.buildSuccess(posts);
    }

    @ApiOperation(value = "Get post detail", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "errors")})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getPostDetail(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                  @PathVariable("id") String postId) {
        return ResponseBuilder.buildSuccess(postService.getPostDetail(currentUser, postId));
    }

    @ApiOperation(value = "Get posts by hashtags", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "errors")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getPostByHashtag(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                     @RequestParam("hashtag") String hashtag) {
        return ResponseBuilder.buildSuccess(postService.getPostsByHashtag(hashtag));
    }

    @ApiOperation(value = "Get most liked posts", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "errors")})
    @GetMapping(value = "/most-liked-posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getMostLikedPosts(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                      @RequestParam("top") int top) {
        return ResponseBuilder.buildSuccess(postService.getMostLikedPosts(top));
    }

    @ApiOperation(value = "Get most commented posts", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "errors")})
    @GetMapping(value = "/most-commented-posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getMostCommentedPosts(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                          @RequestParam("top") int top) {
        return ResponseBuilder.buildSuccess(postService.getMostCommentedPosts(top));
    }

    @ApiOperation(value = "Get most trending hashtags", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "errors")})
    @GetMapping(value = "/trending-hashtags", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getTrendingHashtags(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                        @RequestParam("top") int top) {
        return ResponseBuilder.buildSuccess(postService.getMostTrendingHashtags(top));
    }
}
