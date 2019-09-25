package com.skyteam.skygram.service;

import com.skyteam.skygram.dto.SearchResponseDTO;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    User get(String id);

    UserDTO addUser(UserRequestDTO userRequestDTO);

    Page<UserDTO> getListUsers(UserPrincipal currentUser, Pageable page);

    List<SearchResponseDTO> searchForHome(UserPrincipal currentUser, String q, int top);

    boolean updateUser(UserPrincipal currentUser, UserDTO userDTO);

    boolean follow(UserPrincipal currentUser, String username);

    boolean unfollow(UserPrincipal currentUser, String username);

    UserDTO getUser(UserPrincipal currentUser, String username);

    long getNumOfRegistrations(LocalDate date);

    List<UserDTO> getTopMostFollowedUsers(long top);

    List<UserDTO> getSuggestionUsers(UserPrincipal currentUser, int top);

    List<UserDTO> getMutualFollower(UserPrincipal currentUser, String username);

    long getUsersHaveMoreThanPosts(UserPrincipal currentUser, int numOfPosts);
}
