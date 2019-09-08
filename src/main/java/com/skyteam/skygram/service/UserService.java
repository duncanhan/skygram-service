package com.skyteam.skygram.service;

import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.model.User;

import java.util.List;

public interface UserService {

    Long addUser(User user);

    List<User> getListUsers();

    String createUser(UserDTO userDTO);

    List<UserDTO> search(String term );
}
