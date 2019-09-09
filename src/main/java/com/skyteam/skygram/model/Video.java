package com.skyteam.skygram.model;

public class Video extends Media{
  private double duration;

  public Video(String url) {
    super(url);
  }

  public void stream(){

  }

  public double getDuration() {
    return duration;
  }

  public void setDuration(double duration) {
    this.duration = duration;
  }
}
