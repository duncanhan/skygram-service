package com.skyteam.skygram.controller;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @ApiOperation(value= "Search user profile",
        notes = "Returns a list of users that match the search")
    @GetMapping("/search")
    public Response search(@RequestParam("term") String term) {
        return ResponseBuilder.buildSuccess( userService.search(term));
    }

    @RequestMapping(value = { "/post" }, method = { RequestMethod.POST }, consumes = { "multipart/form-data" })
    public Response post(@RequestParam("files") MultipartFile file, @RequestParam("user") String user,
        @RequestParam("user") String title,
        @RequestParam("localtion") String localtion){
        MultipartFile[] files = {file};
        for(MultipartFile uploadedFile : files) {
            System.out.println(uploadedFile.getOriginalFilename());
        }
        return ResponseBuilder.buildSuccess(userService.createPost(user,title,files,localtion));
    }
}
