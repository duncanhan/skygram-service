package com.skyteam.skygram.service.impl;

import com.skyteam.skygram.dto.UserDTO;
import com.skyteam.skygram.model.Comment;
import com.skyteam.skygram.model.Media;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.User;
import com.skyteam.skygram.repository.UserRepository;
import com.skyteam.skygram.service.UserService;
import com.skyteam.skygram.service.file.FileStorageService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

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
    public String createPost(String user, String title, MultipartFile[] files, String location) {
        //@todo create id
        String id = user.substring(0,2)+""+System.currentTimeMillis();
        Post post = new Post(id, title, LocalDateTime.now(), location,
            new ArrayList<Comment>(),
            new ArrayList<String>(),
            new ArrayList<Media>());

            for(MultipartFile file:files){

                String fileName = fileStorageServ.storeFile(file);
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();

                //@todo Create Media objects and add them to the media array
                
            }
//        Arrays.asList(files)
//            .stream()
//            .map(file -> savePost(file,user))
//            .collect(Collectors.toList());
        return null;
    }




}
