package com.skyteam.skygram.controller;

import com.skyteam.skygram.common.PageDTO;
import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.dto.SearchResponseDTO;
import com.skyteam.skygram.security.CurrentUser;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.PostService;
import com.skyteam.skygram.service.UserService;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @ApiOperation(value = "Search by username or #hashtags", authorizations = @Authorization(value = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Internal errors")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response search(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                           @Valid @RequestParam @NotBlank(message = "Search term is required") String q,
                           @RequestParam("top") int top) {
        List<SearchResponseDTO> list;
        if (q.startsWith("#")) {
            list = postService.searchHashtags(q, top);
        } else {
            list = userService.searchForHome(currentUser, q, top);
        }
        return ResponseBuilder.buildSuccess(list);
    }
}
