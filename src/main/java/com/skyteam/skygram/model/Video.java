package com.skyteam.skygram.model;

import com.skyteam.skygram.service.file.FileType;

public class Video extends Media {

    private double duration;

    public Video(String id, String url, String fileFormat, FileType type, double duration) {
        super(id, url, fileFormat, type);
        this.duration = duration;
    }

    public void stream() {

    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
