package com.skyteam.skygram.controller;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.dto.LoginDTO;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.security.JwtTokenProvider;
import com.skyteam.skygram.security.JwtUserDetailsService;
import com.skyteam.skygram.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad credentials")})
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response login(@ApiParam(value = "Login credentials", required = true) @Valid @RequestBody LoginDTO body) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.getAccount(), body.getPassword()));
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(body.getAccount());
        String token = jwtTokenProvider.generateJwtToken(userDetails);
        return ResponseBuilder.buildSuccess(token);
    }

    @ApiOperation(value = "Register an account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "error")})
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response register(@ApiParam(value = "Account information for registration", required = true) @Valid @RequestBody UserRequestDTO body) {
        UserDTO userDTO = userService.addUser(body);
        return ResponseBuilder.buildSuccess(userDTO);
    }
}
