package com.skyteam.skygram.repository;

import com.skyteam.skygram.base.BaseRepository;
import com.skyteam.skygram.model.User;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, String> {

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Page<User> findByUsernameStartsWith(String q, Pageable pageable);

    @Query(value = "{ _id: ?0 }", fields = "{ followings : 1, _id : 0 }")
    Document findFollowings(String id);

    long countBySignupDateBetween(LocalDateTime start, LocalDateTime end);
}
