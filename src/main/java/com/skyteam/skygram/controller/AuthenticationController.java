package com.skyteam.skygram.controller;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.dto.LoginDTO;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.security.JwtTokenProvider;
import com.skyteam.skygram.security.JwtUserDetailsService;
import com.skyteam.skygram.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginDTO body) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.getAccount(), body.getPassword()));
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(body.getAccount());
        String token = jwtTokenProvider.generateJwtToken(userDetails);
        return ResponseBuilder.buildSuccess(token);
    }

    @ApiOperation(value = "Register an account")
    @PostMapping("/register")
    public Response register(@Valid @RequestBody @NotNull UserRequestDTO body) {
        UserDTO userDTO = userService.addUser(body);
        return ResponseBuilder.buildSuccess(userDTO);
    }
}
