package com.skyteam.skygram.controller;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.service.PostsService;
import com.skyteam.skygram.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostsService postsService;

    @ApiOperation(value = "Get list user", authorizations = { @Authorization(value="apiKey") })
    @GetMapping
    public Response getListUsers() {
        List<User> users = userService.getListUsers();
        return ResponseBuilder.buildSuccess(users);
    }

    @ApiOperation(value= "Search user profile",
        notes = "Returns a list of users that match the search")
    @GetMapping("/search")
    public Response search(@RequestParam("term") String term) {
        return ResponseBuilder.buildSuccess( userService.search(term));
    }

    @GetMapping("/{username}/posts")
    public Response getPostsByUser(@PathVariable String username){
        return ResponseBuilder.buildSuccess(postsService.getPostsByUser(username));
    }
}
