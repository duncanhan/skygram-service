//package com.skyteam.skygram.service.file;
//
//import com.skyteam.skygram.model.Photo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.data.util.Pair;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//@Service
//public class FileStorageService {
//
//  private final Path fileStorageLocation;
//
//  @Autowired
//  public FileStorageService(FileStorageProperties fileStorageProperties) {
//    this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
//        .toAbsolutePath().normalize();
//    try {
//      Files.createDirectories(this.fileStorageLocation);
//    } catch (Exception ex) {
//      throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
//    }
//  }
//
//  public Pair<Boolean,Pair<String,String>> storeFile(MultipartFile file) {
//    // Normalize file name
//    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//    try {
//      // check if file format is allowed
//      if(!isAllowedFormat(fileName)) {
//        return Pair.of(false, Pair.of(fileName, "Sorry! fileformat is not allowed"));
//      }
//      // Check if the file's name contains invalid characters
//      if(fileName.contains("..")) {
//        return Pair.of(false, Pair.of(fileName,"Sorry! Filename contains invalid path sequence " + fileName));
//      }
//      // Copy file to the target location (Replacing existing file with the same name)
//      //@todo provide a url to the file server in the application.properties file, for now we are using local
//      Path targetLocation = this.fileStorageLocation.resolve(fileName);
//      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//      return Pair.of(true, Pair.of(fileName,"File Uploaded"));
//    } catch (IOException ex) {
//      throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//    }
//  }
//
//  private boolean isAllowedFormat(String filename){
//    Photo p = new Photo(filename);
//    return !p.getType().equals(FileType.OTHER);
//  }
//}