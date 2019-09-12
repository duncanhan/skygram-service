package com.skyteam.skygram.model;

import com.skyteam.skygram.service.file.FileType;

public class Photo extends Media {

    public Photo(String id, String url, String fileFormat, FileType type) {
        super(id, url, fileFormat, type);
    }
}
