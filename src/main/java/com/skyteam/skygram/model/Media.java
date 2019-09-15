package com.skyteam.skygram.model;

import com.skyteam.skygram.service.file.FileType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document
public abstract class Media {

    private String id;

    private String url;

    @Field(value = "file_format")
    private String fileFormat;

    private FileType type;

    public Media(String id, String url, String fileFormat, FileType type) {
        this.id = id;
        this.url = url;
        this.fileFormat = fileFormat;
        this.type = type;
    }

    public Media(String url) {
        setUrl(url);
        determineFileType();
    }

    private void determineFileType() {
        String extention = getUrl().substring(getUrl().lastIndexOf(".") + 1);
        if (extention.equalsIgnoreCase("jpg")
                || extention.equalsIgnoreCase("png")
                || extention.equalsIgnoreCase("jpeg")) {
            setType(FileType.PHOTO);
        } else if (extention.equalsIgnoreCase("mp4")
                || extention.equalsIgnoreCase("mwv")
                || extention.equalsIgnoreCase("mov")) {
            setType(FileType.VIDEO);
        } else {
            setType(FileType.OTHER);
        }
    }
}
