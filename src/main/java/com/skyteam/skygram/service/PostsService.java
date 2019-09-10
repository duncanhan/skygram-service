package com.skyteam.skygram.service;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface PostsService {
  Page<PostDTO> getPostsByUser(String username);
  Response createPost(String user, String title, MultipartFile[] files, String localtion, String[] hashtags);
}
