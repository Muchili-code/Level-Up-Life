package com.liferpg.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 根据用户名查找用户，用于登录校验
    Optional<User> findByUsername(String username);
}