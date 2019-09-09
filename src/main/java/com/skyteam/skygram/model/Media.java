package com.skyteam.skygram.model;

import com.skyteam.skygram.service.file.FileType;
import java.awt.PageAttributes.MediaType;

public abstract class Media {
  private String id;
  private String url;
  private String fileFormat;
  private FileType type;
  public Media(String url){
    setUrl(url);
    determineFileType();
  }
  private void determineFileType(){
    String extention = getUrl().substring(getUrl().lastIndexOf(".") + 1);
    if (extention.equalsIgnoreCase("jpg")
      || extention.equalsIgnoreCase("png")
      || extention.equalsIgnoreCase("jpeg")) {
      setType(FileType.PHOTO);
    }else if(extention.equalsIgnoreCase("mp4")
      || extention.equalsIgnoreCase("mwv")
      || extention.equalsIgnoreCase("mov")){
      setType(FileType.VIDEO);
    }else{
      setType(FileType.OTHER);
    }
  }
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getFileFormat() {
    return fileFormat;
  }

  public void setFileFormat(String fileFormat) {
    this.fileFormat = fileFormat;
  }

  public FileType getType() {
    return type;
  }

  public void setType(FileType type) {
    this.type = type;
  }
}
