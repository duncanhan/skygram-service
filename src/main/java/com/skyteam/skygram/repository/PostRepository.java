package com.skyteam.skygram.repository;

import com.skyteam.skygram.base.BaseRepository;
import com.skyteam.skygram.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepository extends BaseRepository<Post, String> {

    Page<Post> findByAuthor(String id, Pageable pageable);

    Page<Post> findByAuthorIn(List<String> authors, Pageable pageable);
}
