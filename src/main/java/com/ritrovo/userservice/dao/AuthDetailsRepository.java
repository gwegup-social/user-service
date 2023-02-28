package com.ritrovo.userservice.dao;

import com.ritrovo.userservice.entity.AuthDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthDetailsRepository extends MongoRepository<AuthDetails, String> {

    Optional<AuthDetails> findByUserId(String userId);

}
