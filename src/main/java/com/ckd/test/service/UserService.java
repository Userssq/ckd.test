package com.ckd.test.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ckd.test.entity.User;
import com.ckd.test.repository.UserRepository;

/**
 * 用户服务类
 * 
 * @author ckd
 * @version 1.0.0
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 保存用户
     * 
     * @param user 用户对象
     * @return 保存后的用户对象
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 根据ID查找用户
     * 
     * @param id 用户ID
     * @return 用户对象
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 统计用户数量
     * 
     * @return 用户总数
     */
    public long countUsers() {
        return userRepository.count();
    }
}
