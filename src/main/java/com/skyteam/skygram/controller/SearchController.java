package com.skyteam.skygram.controller;

import com.skyteam.skygram.common.PageDTO;
import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.dto.SearchResponseDTO;
import com.skyteam.skygram.service.UserService;
import com.skyteam.skygram.util.PageUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Search by username or #hashtags", authorizations = @Authorization(value = "apiKey"))
    @GetMapping
    public Response search(@Valid @RequestParam @NotBlank(message = "Search term is required") String q,
                           @ModelAttribute PageDTO pageDTO) {
        Page<SearchResponseDTO> page = null;
        if (q.startsWith("#")) {
            // TODO search hashtags
        } else {
            page = userService.searchForHome(q, PageUtil.initPage(pageDTO, new Sort(Sort.Direction.ASC, "username")));
        }
        return ResponseBuilder.buildSuccess(page);
    }
}
