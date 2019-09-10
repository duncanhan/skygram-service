package com.skyteam.skygram.service.impl;

import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.repository.UserRepository;
import com.skyteam.skygram.service.UserService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO addUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setSignupDate(LocalDateTime.now());
        user.setRoles(Collections.singletonList("USER"));
        userRepository.save(user);
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthday(),
                user.getSignupDate()
        );
    }

    @Override
    public List<User> getListUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTO> search(String term) {
        return userRepository.search(term);
    }

}
