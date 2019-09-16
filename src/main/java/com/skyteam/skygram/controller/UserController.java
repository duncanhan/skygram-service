package com.skyteam.skygram.controller;

import com.skyteam.skygram.common.PageDTO;
import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.dto.PostDTO;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.exception.AppException;
import com.skyteam.skygram.security.CurrentUser;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.PostService;
import com.skyteam.skygram.service.UserService;
import com.skyteam.skygram.util.PageUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @ApiOperation(value = "Get list user", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Internal errors")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getListUsers(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                 @ModelAttribute PageDTO pageDTO) {
        Page<UserDTO> users = userService.getListUsers(currentUser, PageUtil.initPage(pageDTO));
        return ResponseBuilder.buildSuccess(users);
    }

    @ApiOperation(value = "Update profile", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Internal errors")})
    @PutMapping(value = "/me/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updateProfile(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                  @ApiParam(value = "User profile information", required = true) @Valid @RequestBody UserDTO body) {
        userService.updateUser(currentUser, body);
        return ResponseBuilder.buildSuccess("User is updated");
    }

    @ApiOperation(value = "Follow an user", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Username not found"),
            @ApiResponse(code = 500, message = "Internal errors")})
    @PostMapping(value = "/{username}/follow", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response follow(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                           @Valid @PathVariable("username") @NotBlank(message = "Username is required") String username) {
        if (currentUser != null && currentUser.getUsername().equals(username)) {
            throw new AppException("Can not follow yourself");
        }
        userService.follow(currentUser, username);
        return ResponseBuilder.buildSuccess();
    }

    @ApiOperation(value = "Unfollow an user", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Username not found"),
            @ApiResponse(code = 500, message = "Internal errors")})
    @PostMapping(value = "/{username}/unfollow", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response unfollow(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                             @Valid @PathVariable("username") @NotBlank(message = "Username is required") String username) {
        if (currentUser != null && currentUser.getUsername().equals(username)) {
            throw new AppException("Can not unfollow yourself");
        }
        userService.unfollow(currentUser, username);
        return ResponseBuilder.buildSuccess();
    }

    @ApiOperation(value = "Get user profile", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Username not found"),
            @ApiResponse(code = 500, message = "Internal errors")})
    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getUserProfile(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                   @Valid @PathVariable("username") @NotBlank(message = "Username is required") String username) {
        return ResponseBuilder.buildSuccess(userService.getUser(currentUser, username));
    }

    @ApiOperation(value = "Get posts by username", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Username not found"),
            @ApiResponse(code = 500, message = "Internal errors")})
    @GetMapping(value = "/{username}/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getPostsByUsername(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                       @PathVariable("username") String username,
                                       @ModelAttribute PageDTO pageDTO) {
        Page<PostDTO> page = postService.getPostsByUser(currentUser, username, PageUtil.initPage(pageDTO, new Sort(Direction.DESC, "date")));
        return ResponseBuilder.buildSuccess(page);
    }
}
