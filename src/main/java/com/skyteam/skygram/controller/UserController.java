package com.skyteam.skygram.controller;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Response getListUsers() {
        List<User> users = userService.getListUsers();
        return ResponseBuilder.buildSuccess(users);
    }

    @PostMapping
    public Response createUser(@RequestBody UserDTO userDTO) {
        String email = userService.createUser(userDTO);
        return ResponseBuilder.buildSuccess(email);
    }

    @GetMapping("/search")
    public Response search() {
        return ResponseBuilder.buildSuccess("seach endpoint");
    }
}
