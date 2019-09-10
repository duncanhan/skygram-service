package com.skyteam.skygram.service.impl;

import com.skyteam.skygram.dto.PostDTO;
import com.skyteam.skygram.repository.PostRepository;
import com.skyteam.skygram.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImp implements PostsService {

  @Autowired
  PostRepository postRepository;

  @Override
  public Page<PostDTO> getPostsByUser(String username) {
    return postRepository.getUserPosts(username,PageRequest.of(0,10,new Sort(Direction.DESC,"date")));
  }
}
