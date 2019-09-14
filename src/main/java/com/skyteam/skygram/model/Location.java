package com.skyteam.skygram.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Location {

    @Field(value = "longitude")
    private String longitude;

    @Field(value = "latitude")
    private String latitude;

    public Location(String[] locations) {
        if (locations == null || locations.length != 2) return;
        this.longitude = locations[0];
        this.latitude = locations[1];
    }
}
