package com.skyteam.skygram.service;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.model.User;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserDTO addUser(UserRequestDTO userRequestDTO);

    List<User> getListUsers();

    List<UserDTO> search(String term );

    Response createPost(String user, String title, MultipartFile[] files, String localtion);
}
