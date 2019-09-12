package com.skyteam.skygram.service;

import com.skyteam.skygram.dto.SearchResponseDTO;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserDTO addUser(UserRequestDTO userRequestDTO);

    Page<UserDTO> getListUsers(Pageable page);

    Page<UserDTO> search(String q, Pageable page);

    Page<SearchResponseDTO> searchForHome(String q, Pageable page);

    void updateUser(UserPrincipal currentUser, UserDTO userDTO);

    void follow(UserPrincipal currentUser, String username);

    void unfollow(UserPrincipal currentUser, String username);

    UserDTO getUser(String username);
}
