package com.skyteam.skygram.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Location {

    @Field(value = "longitude")
    private String longitude;

    @Field(value = "latitude")
    private String latitude;

    public Location() {
    }

    public Location(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(String[] locations) {
        if (locations == null || locations.length != 2) return;
        this.longitude = locations[0];
        this.latitude = locations[1];
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
