package com.skyteam.skygram.base;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<M, ID extends Serializable> extends MongoRepository<M, ID> {
}
