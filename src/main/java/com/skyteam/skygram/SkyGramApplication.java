package com.skyteam.skygram;

import com.skyteam.skygram.core.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class SkyGramApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyGramApplication.class, args);
    }

}
