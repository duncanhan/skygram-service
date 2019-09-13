package com.skyteam.skygram.repository;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.skyteam.skygram.base.BaseRepository;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.model.User;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends BaseRepository<User, String> {

    @Query("{'username':{ $regex: ?0 }}")
    List<UserDTO> search(String term);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Page<User> findByUsernameStartsWith(String q, Pageable pageable);

    String findUserIdByUsername(String username);

    @Query(value = "{ _id: ?0 }", fields = "{ followings : 1, _id : 0 }")
    Document findFollowings(String id);
}
