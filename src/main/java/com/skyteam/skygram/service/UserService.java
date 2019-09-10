package com.skyteam.skygram.service;

import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.model.User;
import java.util.List;

public interface UserService {

    UserDTO addUser(UserRequestDTO userRequestDTO);

    List<User> getListUsers();

    List<UserDTO> search(String term );
}
