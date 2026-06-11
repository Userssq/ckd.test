package com.ckd.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ckd.test.entity.User;

/**
 * 用户数据访问接口
 * 
 * @author ckd
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱地址
     * @return 用户对象
     */
    Optional<User> findByEmail(String email);
}
