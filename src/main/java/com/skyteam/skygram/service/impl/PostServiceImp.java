package com.skyteam.skygram.service.impl;

import com.skyteam.skygram.core.Response;
import com.skyteam.skygram.core.ResponseBuilder;
import com.skyteam.skygram.core.ResponseCode;
import com.skyteam.skygram.dto.PostDTO;
import com.skyteam.skygram.model.Photo;
import com.skyteam.skygram.model.Post;
import com.skyteam.skygram.model.Video;
import com.skyteam.skygram.repository.PostRepository;
import com.skyteam.skygram.service.PostsService;
import com.skyteam.skygram.service.file.FileStorageService;
import com.skyteam.skygram.service.file.FileType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostServiceImp implements PostsService {

  @Autowired
  PostRepository postRepository;

  @Autowired
  private FileStorageService fileStorageServ;

  @Override
  public Page<PostDTO> getPostsByUser(String username) {
    return postRepository.getUserPosts(username,PageRequest.of(0,10,new Sort(Direction.DESC,"date")));
  }


  @Override
  public Response createPost(String user, String title, MultipartFile[] files, String location, String[] hashtags) {
    if(files.length==0){
      return ResponseBuilder
          .buildFail(ResponseCode.INTERNAL_SERVER_ERROR,"Please chose some files");
    }

    String id = user.substring(0,2)+""+System.currentTimeMillis();
//    Post post = new Post(id, user, title, LocalDateTime.now(), location,new ArrayList<>(),hashtags);
//    int counter = 1;
//    String errorMessage = null;
//
//    for(MultipartFile file:files){
//      Pair<Boolean, Pair<String,String>> result  = fileStorageServ.storeFile(file);
//      if(!result.getFirst()){
//        errorMessage = result.getSecond().getSecond();
//        continue;
//      }
//
//      Photo photo = new Photo(result.getSecond().getFirst());
//      if(photo.getType().equals(FileType.PHOTO)){
//        photo.setId(id+"_"+counter);
//        post.getMedia().add(photo);
//      }else{
//        Video video = new Video(result.getSecond().getFirst());
//        video.setId(id+"_"+counter);
//        post.getMedia().add(video);
//      }
//      counter+=1;
//    }
//
//    if(post.getMedia().isEmpty()){
//      return ResponseBuilder.buildFail(ResponseCode.INTERNAL_SERVER_ERROR,errorMessage);
//    }
//    postRepository.save(post);
    return ResponseBuilder.buildSuccess("Post created successfully");
  }
}
