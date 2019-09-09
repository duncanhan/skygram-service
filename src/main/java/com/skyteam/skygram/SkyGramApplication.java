package com.skyteam.skygram;

import com.skyteam.skygram.core.FileStorageProperties;
import javax.servlet.MultipartConfigElement;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class SkyGramApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyGramApplication.class, args);
    }
}
