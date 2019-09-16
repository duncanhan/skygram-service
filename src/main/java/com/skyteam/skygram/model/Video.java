package com.skyteam.skygram.model;

import com.skyteam.skygram.enumerable.FileType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Video extends Media {

    private double duration;

    public Video(String id, String url, String fileFormat, String type, double duration) {
        super(id, url, fileFormat, type);
        this.duration = duration;
    }
}
