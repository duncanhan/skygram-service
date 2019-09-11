package com.skyteam.skygram.service.impl;

import com.skyteam.skygram.common.Constants;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.exception.ResourceNotFoundException;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.repository.UserRepository;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.UserService;
import com.skyteam.skygram.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User get(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Override
    public UserDTO addUser(UserRequestDTO userRequestDTO) {
        User user = Mapper.map(userRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setSignupDate(LocalDateTime.now());
        user.setRoles(Collections.singletonList(Constants.ROLE_USER));
        userRepository.save(user);
        return Mapper.map(user, UserDTO.class);
    }

    @Override
    public Page<UserDTO> getListUsers(Pageable page) {
        Page<User> users = userRepository.findAll(page);
        return Mapper.mapPage(users, UserDTO.class);
    }

    @Override
    public List<UserDTO> search(String term) {
        return userRepository.search(term);
    }

    @Override
    public void updateUser(UserPrincipal currentUser, UserDTO userDTO) {
        User user = this.get(currentUser.getId());
        if (StringUtils.hasText(userDTO.getFirstName())) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (StringUtils.hasText(userDTO.getLastName())) {
            user.setFirstName(userDTO.getLastName());
        }
        if (userDTO.getBirthday() != null) {
            user.setBirthday(userDTO.getBirthday());
        }
        userRepository.save(user);
    }

    @Override
    public void follow(UserPrincipal currentUser, String username) {
        User user = this.get(currentUser.getId());
        if (userRepository.existsByUsername(username)) {
            user.getFollowers().add(userRepository.findByUsername(username).get().getId());
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("User", "username", username);
        }
    }

    @Override
    public void unfollow(UserPrincipal currentUser, String username) {
        User user = this.get(currentUser.getId());
        if (userRepository.existsByUsername(username)) {
            if (!CollectionUtils.isEmpty(user.getFollowers())) {
                for (String follower : user.getFollowers()) {
                    if (follower.equals(currentUser.getId())) {
                        user.getFollowers().remove(follower);
                        userRepository.save(user);
                        break;
                    }
                }
            }
        } else {
            throw new ResourceNotFoundException("User", "username", username);
        }
    }
}
