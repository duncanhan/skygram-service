package com.skyteam.skygram.service.impl;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.core.ResponseCode;
import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.model.Photo;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.model.Video;
import com.skyteam.skygram.repository.PostRepository;
import com.skyteam.skygram.repository.UserRepository;
import com.skyteam.skygram.service.UserService;
import com.skyteam.skygram.service.file.FileStorageService;
import com.skyteam.skygram.service.file.FileType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    private FileStorageService fileStorageServ;

    @Override
    public Long addUser(User user) {
        return null;
    }

    @Override
    public List<User> getListUsers() {
        return userRepository.findAll();
    }

    @Override
    public String createUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        userRepository.save(user);
        return user.getEmail();
    }

    @Override
    public List<UserDTO> search(String term) {
       return userRepository.search(term);
    }

    @Override
    public Response createPost(String user, String title, MultipartFile[] files, String location) {
        if(files.length==0){
            return ResponseBuilder.buildFail(ResponseCode.INTERNAL_SERVER_ERROR,"Please chose some files");
        }
        String id = user.substring(0,2)+""+System.currentTimeMillis();
        Post post = new Post(id, title, LocalDateTime.now(), location,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>());
        int counter = 1;
        String errorMessage = null;
        for(MultipartFile file:files){
            Pair<Boolean, Pair<String,String>> result  = fileStorageServ.storeFile(file);
            if(!result.getFirst()){
                errorMessage = result.getSecond().getSecond();
                continue;
            }

            Photo photo = new Photo(result.getSecond().getFirst());
            if(photo.getType().equals(FileType.PHOTO)){
                photo.setId(id+"_"+counter);
                post.getMedia().add(photo);
            }else{
                Video video = new Video(result.getSecond().getFirst());
                video.setId(id+"_"+counter);
                post.getMedia().add(video);
            }
            counter+=1;
        }
        if(post.getMedia().isEmpty()){
            return ResponseBuilder.buildFail(ResponseCode.INTERNAL_SERVER_ERROR,errorMessage);
        }
        postRepository.save(post);
        return ResponseBuilder.buildSuccess("Post created successfully");
    }




}
