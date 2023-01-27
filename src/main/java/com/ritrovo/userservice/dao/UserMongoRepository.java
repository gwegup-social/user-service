package com.ritrovo.userservice.dao;

import com.ritrovo.userservice.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserMongoRepository extends MongoRepository<User, String> {

    @Query("{$or : [\n" +
            "  {corporateEmail : ?0},\n" +
            "  {personalEmail : ?0}\n" +
            "  ]}")
    List<User> findUserByEmail(String email);
}
