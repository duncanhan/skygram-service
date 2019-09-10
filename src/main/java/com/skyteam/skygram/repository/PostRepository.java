package com.skyteam.skygram.repository;

import com.skyteam.skygram.base.BaseRepository;
import com.skyteam.skygram.dto.PostDTO;
import com.skyteam.skygram.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

public interface PostRepository extends BaseRepository<Post, String>{
  @Query("{username: ?0}")
  Page<PostDTO> getUserPosts(String username,Pageable pageable);
}
