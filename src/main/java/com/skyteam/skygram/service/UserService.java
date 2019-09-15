package com.skyteam.skygram.service;

import com.skyteam.skygram.dto.SearchResponseDTO;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UserService {

    UserDTO addUser(UserRequestDTO userRequestDTO);

    Page<UserDTO> getListUsers(UserPrincipal currentUser, Pageable page);

    Page<SearchResponseDTO> searchForHome(String q, Pageable page);

    boolean updateUser(UserPrincipal currentUser, UserDTO userDTO);

    boolean follow(UserPrincipal currentUser, String username);

    boolean unfollow(UserPrincipal currentUser, String username);

    UserDTO getUser(UserPrincipal currentUser, String username);

    long getNumOfRegistrations(LocalDate date);
}
