package com.skyteam.skygram.controller;

import com.skyteam.skygram.common.PageDTO;
import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.exception.AppException;
import com.skyteam.skygram.security.CurrentUser;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.PostsService;
import com.skyteam.skygram.service.UserService;
import com.skyteam.skygram.util.PageUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private PostsService postsService;

    @ApiOperation(value = "Get list user", authorizations = {@Authorization(value = "apiKey")})
    @GetMapping
    public Response getListUsers(@ModelAttribute PageDTO pageDTO) {
        Page<UserDTO> users = userService.getListUsers(PageUtil.initPage(pageDTO));
        return ResponseBuilder.buildSuccess(users);
    }

    @ApiOperation(value = "Update profile", authorizations = {@Authorization(value = "apiKey")})
    @PutMapping("/me/profile")
    public Response updateProfile(@ApiIgnore UserPrincipal currentUser, @Valid @RequestBody UserDTO userDTO) {
        userService.updateUser(currentUser, userDTO);
        return ResponseBuilder.buildSuccess("User is updated");
    }

    @ApiOperation(value = "Follow an user", authorizations = {@Authorization(value = "apiKey")})
    @PostMapping("/{username}/follow")
    public Response follow(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                           @Valid @PathVariable("username") @NotBlank(message = "Username is required") String username) {
        if (currentUser.getUsername().equals(username)) {
            throw new AppException("Can not follow yourself");
        }
        userService.follow(currentUser, username);
        return ResponseBuilder.buildSuccess();
    }

    @ApiOperation(value = "Unfollow an user", authorizations = {@Authorization(value = "apiKey")})
    @PostMapping("/{username}/unfollow")
    public Response unfollow(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                             @Valid @PathVariable("username") @NotBlank(message = "Username is required") String username) {
        if (currentUser.getUsername().equals(username)) {
            throw new AppException("Can not unfollow yourself");
        }
        userService.unfollow(currentUser, username);
        return ResponseBuilder.buildSuccess();
    }

//    @ApiOperation(value= "Search user profile",
//        notes = "Returns a list of users that match the search")
//    @GetMapping("/search")
//    public Response search(@RequestParam("term") String term) {
//        return ResponseBuilder.buildSuccess( userService.search(term));
//    }
//
//    @GetMapping("/{username}/posts")
//    public Response getPostsByUser(@PathVariable String username){
//        return ResponseBuilder.buildSuccess(postsService.getPostsByUser(username));
//    }
}
