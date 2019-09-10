package com.skyteam.skygram.repository;

import com.skyteam.skygram.base.BaseRepository;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends BaseRepository<User, Long> {

    @Query("{'username':{ $regex: ?0 }}")
    List<UserDTO> search(String term);

    Optional<User> findByUsernameOrEmail(String username, String email);
}
