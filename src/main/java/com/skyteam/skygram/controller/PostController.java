package com.skyteam.skygram.controller;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.security.CurrentUser;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.impl.PostServiceImp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/posts")
public class PostController {
  @Autowired
  PostServiceImp postService;

  @ApiOperation(value="Post", authorizations = {@Authorization(value = "apiKey")})
  @RequestMapping(value = { "/post" }, method = { RequestMethod.POST }, consumes = { "multipart/form-data" })
  public Response post(@ApiIgnore @CurrentUser UserPrincipal currentUser,@RequestParam("files") MultipartFile file,
      @RequestParam("title") String title,
      @RequestParam("localtion") String[] localtion,
      @RequestParam("hashtags") List<String> hashtags){
    MultipartFile[] files = {file};
    return ResponseBuilder.buildSuccess(postService.createPost(currentUser.getId(),title,files,localtion,hashtags));
  }

}
