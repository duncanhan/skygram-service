package com.skyteam.skygram.service;

import com.skyteam.skygram.dto.PostDTO;
import org.springframework.data.domain.Page;

public interface PostsService {
  Page<PostDTO> getPostsByUser(String username);
}
