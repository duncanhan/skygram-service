package com.skyteam.skygram.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.skyteam.skygram.dto.CommentDTO;
import com.skyteam.skygram.dto.CommentRequestDTO;
import com.skyteam.skygram.dto.PostDTO;
import com.skyteam.skygram.dto.SearchResponseDTO;
import com.skyteam.skygram.exception.AppException;
import com.skyteam.skygram.exception.NoPermissionException;
import com.skyteam.skygram.exception.ResourceNotFoundException;
import com.skyteam.skygram.model.*;
import com.skyteam.skygram.repository.PostRepository;
import com.skyteam.skygram.repository.UserRepository;
import com.skyteam.skygram.security.UserPrincipal;
import com.skyteam.skygram.service.PostService;
import com.skyteam.skygram.service.file.FileType;
import com.skyteam.skygram.util.Mapper;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Value("${cloudinary.folder}")
    private String folder;

    private Post get(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }

    @Override
    public Page<PostDTO> getPostsByUser(UserPrincipal currentUser, String username, Pageable pageable) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        Page<Post> page = postRepository.findByAuthor(author.getId(), pageable);
        return this.mapPage(page, currentUser.getId());
    }

    @Override
    public PostDTO createPost(UserPrincipal currentUser, String title, MultipartFile[] files, String[] location, String[] hashtags) throws IOException {
        Post post = new Post(currentUser.getId(), title, hashtags != null ? new HashSet<>(Arrays.asList(hashtags)) : new HashSet<>(), new Location(location));
        postRepository.save(post);

        Media media;
        int count = 0;
        for (MultipartFile file : files) {
            media = this.upload(file, ++count, post.getId());
            post.addMedia(media);
        }
        if (post.getMedia().isEmpty()) {
            throw new AppException("Please add an image");
        }
        post = postRepository.save(post);
        return Mapper.map(post, PostDTO.class);
    }

    @Override
    public boolean updatePost(UserPrincipal currentUser, String postId, String title, MultipartFile[] files, String[] location, String[] hashtags) throws IOException {
        Post post = this.checkPermission(currentUser.getId(), postId);
        post.setTitle(title);
        post.setLocation(new Location(location));
        post.setHashtags(new HashSet<>(Arrays.asList(hashtags)));
        Media media;
        for (MultipartFile file : files) {
            media = this.upload(file, 1, postId);
            post.updateMedia(media);
        }
        if (post.getMedia().isEmpty()) {
            throw new AppException("Please add an image");
        }
        postRepository.save(post);
        return true;
    }

    @Override
    public boolean deletePost(UserPrincipal currentUser, String postId) {
        Post post = this.checkPermission(currentUser.getId(), postId);
        postRepository.delete(post);
        return true;
    }

    @Override
    public CommentDTO createComment(UserPrincipal currentUser, String postId, CommentRequestDTO commentRequestDTO) {
        Post post = this.get(postId);
        Comment comment = Mapper.map(commentRequestDTO, Comment.class);
        comment.setId(new ObjectId());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setLastModifiedDate(LocalDateTime.now());
        comment.setAuthor(new User(currentUser.getId()));
        post.addComment(comment);
        postRepository.save(post);
        return Mapper.map(comment, CommentDTO.class);
    }

    @Override
    public boolean updateComment(UserPrincipal currentUser, String postId, String commentId, CommentRequestDTO commentRequestDTO) {
        Post post = this.get(postId);
        post.updateComment(commentId, currentUser.getId(), commentRequestDTO);
        postRepository.save(post);
        return true;
    }

    @Override
    public boolean deleteComment(UserPrincipal currentUser, String postId, String commentId) throws ResourceNotFoundException {
        Post post = this.get(postId);
        post.deleteComment(commentId, currentUser.getId());
        postRepository.save(post);
        return true;
    }

    @Override
    public boolean like(UserPrincipal currentUser, String postId) {
        Post post = this.get(postId);
        post.likedBy(currentUser.getId());
        postRepository.save(post);
        return true;
    }

    @Override
    public boolean unlike(UserPrincipal currentUser, String postId) {
        Post post = this.get(postId);
        post.unlikeBy(currentUser.getId());
        postRepository.save(post);
        return true;
    }

    @Override
    public Page<SearchResponseDTO> searchHashtags(String q, Pageable page) {
//        Page<User> users = postRepository.findByUsernameStartsWith(q, page);
//        return users.map(user -> new SearchResponseDTO(user.getId(), null, user.getUsername(), user.getFirstName() + " " + user.getLastName()));
        return null;
    }

    @Override
    public Page<PostDTO> getTimelinePosts(UserPrincipal currentUser, Pageable page) {
        List<String> authors = new ArrayList<>();
        authors.add(currentUser.getId());
        Document document = userRepository.findFollowings(currentUser.getId());
        List<String> followings = (List<String>) document.get("followings");
        if (followings!= null && !followings.isEmpty()) {
            authors.addAll(followings);
        }
        Page<Post> posts = postRepository.findByAuthorIn(authors, page);
        return this.mapPage(posts, currentUser.getId());
    }

    @Override
    public PostDTO getPostDetail(UserPrincipal currentUser, String postId) {
        Post post = this.get(postId);
        PostDTO postDTO = Mapper.map(post, PostDTO.class);
        if (post.getLikes() != null && post.getLikes().contains(currentUser.getId())) {
            postDTO.setLiked(true);
        }
        if (post.getAuthor().getId().equals(currentUser.getId())) {
            postDTO.setOwned(true);
        }
        return postDTO;
    }

    @Override
    public long getNumOfPosts(LocalDate date) {
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
        return postRepository.countByPostedDateBetween(start, end);
    }

    /**
     * Check if post is allowed modify from user
     *
     * @param userId user id
     * @param postId post id
     * @return post
     */
    private Post checkPermission(String userId, String postId) {
        Post post = this.get(postId);
        if (!post.getAuthor().getId().equals(userId)) {
            throw new NoPermissionException("You DO NOT have permission to do this action");
        }
        return post;
    }

    /**
     * Upload media file to Cloudinary
     *
     * @param file file to upload
     * @return result
     * @throws IOException exception
     */
    private Media upload(MultipartFile file, int count, String postId) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", postId + "_" + count,
                        "folder", folder + postId
                ));
        if (uploadResult == null) {
            throw new AppException("Error in uploading media file(s)");
        }
        Media media;
        if (uploadResult.get("resource_type").toString().equals("image")) {
            media = new Photo(postId + "_" + count, uploadResult.get("url").toString(), uploadResult.get("format").toString(), FileType.PHOTO);
        } else if (uploadResult.get("resource_type").toString().equals("video")) {
            media = new Video(postId + "_" + count, uploadResult.get("url").toString(), uploadResult.get("format").toString(), FileType.VIDEO, 0);
        } else {
            media = new Photo(postId + "_" + count, uploadResult.get("url").toString(), uploadResult.get("format").toString(), FileType.OTHER);
        }
        return media;
    }

    /**
     * Map page post to postDTO
     * @param posts
     * @param currentUserId
     * @return
     */
    private Page<PostDTO> mapPage(Page<Post> posts, String currentUserId) {
        if (posts == null) return null;
        return posts.map(post -> {
            PostDTO dto = Mapper.map(post, PostDTO.class);
            if (post.getLikes() != null && post.getLikes().contains(currentUserId)) {
                dto.setLiked(true);
            }
            if (post.getAuthor().getId().equals(currentUserId)) {
                dto.setOwned(true);
            }
            return dto;
        });
    }
}
