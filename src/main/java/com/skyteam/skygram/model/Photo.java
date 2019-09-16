package com.skyteam.skygram.model;

import com.skyteam.skygram.enumerable.FileType;

public class Photo extends Media {

    public Photo(String id, String url, String fileFormat, String type) {
        super(id, url, fileFormat, type);
    }
}
