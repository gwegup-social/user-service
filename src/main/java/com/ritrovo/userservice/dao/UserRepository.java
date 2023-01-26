package com.ritrovo.userservice.dao;

import com.ritrovo.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {


    @Query(value = "SELECT * FROM user WHERE EMAIL = ?1", nativeQuery = true)
    User findByEmail(String emailAddress);

    User findByPhone(String phone);

    @Modifying
    @Query(value = "UPDATE user SET corporate_email = ?2, company_name = ?3, last_updated = ?4 where id = ?1", nativeQuery = true)
    void updateCorporateEmailandCompanyName(int id, String email, String company, Date updated_at);

    @Modifying
    @Query(value = "UPDATE user SET status = ?2 where id = ?1", nativeQuery = true)
    void updateStatus(int id, String status);
}
