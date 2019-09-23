package com.skyteam.skygram.service.impl;

import com.skyteam.skygram.common.Constants;
import com.skyteam.skygram.dto.SearchResponseDTO;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.dto.UserRequestDTO;
import com.skyteam.skygram.exception.ResourceNotFoundException;
import com.skyteam.skygram.functional.CommonFunctional;
import com.skyteam.skygram.functional.UserFunctional;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.repository.UserRepository;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.UserService;
import com.skyteam.skygram.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User get(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Override
    public UserDTO addUser(UserRequestDTO userRequestDTO) {
        User user = Mapper.map(userRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setSignupDate(LocalDateTime.now());
        user.setRoles(Collections.singletonList(Constants.ROLE_USER));
        user = userRepository.save(user);
        return Mapper.map(user, UserDTO.class);
    }

    @Override
    public Page<UserDTO> getListUsers(UserPrincipal currentUser, Pageable page) {
        List<User> allUsers = userRepository.findAll();
        List<User> users = CommonFunctional.pagingUser.apply(allUsers, page);
        return this.mapPage(new PageImpl<>(users, page, allUsers.size()), currentUser.getId());
    }

    @Override
    public Page<SearchResponseDTO> searchForHome(String q, Pageable page) {
//        Page<User> users = userRepository.findByUsernameStartsWith(q, page);
//        return users.map(user -> new SearchResponseDTO(user.getId(), null, user.getUsername(), user.getFirstName() + " " + user.getLastName()));
        //TODO
        return null;
    }

    @Override
    public boolean updateUser(UserPrincipal currentUser, UserDTO userDTO) {
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
        return true;
    }

    @Override
    public boolean follow(UserPrincipal currentUser, String username) {
        User follower = this.get(currentUser.getId());
        User followee = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        follower.follow(followee);
        userRepository.save(followee);
        userRepository.save(follower);
        return true;
    }

    @Override
    public boolean unfollow(UserPrincipal currentUser, String username) {
        User follower = this.get(currentUser.getId());
        User followee = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        follower.unfollow(followee);
        userRepository.save(follower);
        return true;
    }

    @Override
    public UserDTO getUser(UserPrincipal currentUser, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        UserDTO dto = Mapper.map(user, UserDTO.class);
        if (user.getFollowings() != null && user.getFollowings().contains(currentUser.getId())) {
            dto.setFollowed(true);
        }
        if (user.getFollowers() != null && user.getFollowers().contains(currentUser.getId())) {
            dto.setFollowing(true);
        }
        return dto;
    }

    @Override
    public long getNumOfRegistrations(LocalDate date) {
        List<User> users = userRepository.findAll();
        return UserFunctional.NUM_OF_REGISTRATIONS.apply(users, date);
    }

    /**
     * Map page user to userDTO
     *
     * @param users         users page
     * @param currentUserId current userId
     * @return userDTO page
     */
    private Page<UserDTO> mapPage(Page<User> users, String currentUserId) {
        if (users == null) return null;
        return users.map(user -> {
            UserDTO dto = Mapper.map(user, UserDTO.class);
            if (user.getFollowings() != null && user.getFollowings().contains(currentUserId)) {
                dto.setFollowed(true);
            }
            if (user.getFollowers() != null && user.getFollowers().contains(currentUserId)) {
                dto.setFollowing(true);
            }
            return dto;
        });
    }
}
