package com.skyteam.skygram.service.impl;

import com.skyteam.skygram.dto.CommentDTO;
import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.dto.PostDTO;
import com.skyteam.skygram.dto.PostRequestDTO;
import com.skyteam.skygram.exception.AppException;
import com.skyteam.skygram.exception.ResourceNotFoundException;
import com.skyteam.skygram.model.*;
import com.skyteam.skygram.repository.PostRepository;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.PostService;
import com.skyteam.skygram.service.file.FileStorageService;
import com.skyteam.skygram.service.file.FileType;
import com.skyteam.skygram.util.Mapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class PostServiceImp implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    private FileStorageService fileStorageServ;

    private Post get(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }

    @Override
    public Page<PostDTO> getPostsByUser(String username) {
        return postRepository.getUserPosts(username, PageRequest.of(0, 10, new Sort(Direction.DESC, "date")));
    }

    @Override
    public String createPost(String user, String title, MultipartFile[] files, String[] location, List<String> hashtags) {
        if (files.length == 0) {
//      return ResponseBuilder
//          .buildFail(ResponseCode.INTERNAL_SERVER_ERROR,"Please chose some files");
            throw new AppException("Please chose some files");
        }

        Post post = new Post(user, title, new HashSet<>(hashtags), new Location(location));
        int counter = 1;
        String errorMessage = null;

        for (MultipartFile file : files) {
            Pair<Boolean, Pair<String, String>> result = fileStorageServ.storeFile(file);
            if (!result.getFirst()) {
                errorMessage = result.getSecond().getSecond();
                continue;
            }

            postRepository.save(post);

            Photo photo = new Photo(result.getSecond().getFirst());
            if (photo.getType().equals(FileType.PHOTO)) {
                photo.setId(post.getId() + "_" + counter);
                post.getMedias().add(photo);
            } else {
                Video video = new Video(result.getSecond().getFirst());
                video.setId(post.getId() + "_" + counter);
                post.getMedias().add(video);
            }
            counter += 1;
        }

        if (post.getMedias().isEmpty()) {
            throw new AppException("Please add an image");
        }
        postRepository.save(post);
        return post.getId();
    }

    @Override
    public void updatePost(UserPrincipal currentUser, String postId, PostRequestDTO postRequestDTO) {
        Post post = this.checkPermission(currentUser.getId(), postId);
        post.setTitle(postRequestDTO.getTitle());
        post.setLocation(new Location(postRequestDTO.getLocation()));
        post.setHashtags(postRequestDTO.getHashtags());
        postRepository.save(post);
    }

    @Override
    public void deletePost(UserPrincipal currentUser, String postId) {
        Post post = this.checkPermission(currentUser.getId(), postId);
        postRepository.delete(post);
    }

    @Override
    public CommentDTO createComment(UserPrincipal currentUser, String postId, CommentRequestDTO commentRequestDTO) {
        Post post = this.checkPermission(currentUser.getId(), postId);
        Comment comment = Mapper.map(commentRequestDTO, Comment.class);
        comment.setId(new ObjectId());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setLastModifiedDate(LocalDateTime.now());
        comment.setAuthor(currentUser.getId());
        post.addComment(comment);
        postRepository.save(post);
        return Mapper.map(comment, CommentDTO.class);
    }

    @Override
    public void updateComment(UserPrincipal currentUser, String postId, String commentId, CommentRequestDTO commentRequestDTO) {
        Post post = this.checkPermission(currentUser.getId(), postId);
        post.updateComment(commentId, commentRequestDTO);
        postRepository.save(post);
    }

    @Override
    public void deleteComment(UserPrincipal currentUser, String postId, String commentId) throws ResourceNotFoundException {
        Post post = this.checkPermission(currentUser.getId(), postId);
        post.deleteComment(commentId);
        postRepository.save(post);
    }

    @Override
    public void like(UserPrincipal currentUser, String postId) {
        Post post = this.checkPermission(currentUser.getId(), postId);
        post.likedBy(currentUser.getId());
        postRepository.save(post);
    }

    @Override
    public void unlike(UserPrincipal currentUser, String postId) {
        Post post = this.checkPermission(currentUser.getId(), postId);
        post.unlikeBy(currentUser.getId());
        postRepository.save(post);
    }

    /**
     * Check if post is allowed modify from user
     * @param userId user id
     * @param postId post id
     * @return post
     */
    private Post checkPermission(String userId, String postId) {
        Post post = this.get(postId);
        if (!post.getAuthor().equals(userId)) {
            throw new AppException("You DO NOT have permission to do this action");
        }
        return post;
    }
}
