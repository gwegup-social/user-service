package com.ritrovo.userservice.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends UserMongoRepository,UserCustomRepository {


}
