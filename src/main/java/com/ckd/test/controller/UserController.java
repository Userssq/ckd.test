package com.ckd.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ckd.test.entity.User;
import com.ckd.test.service.UserService;

/**
 * 用户控制器
 * 
 * @author ckd
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 根据ID获取用户
     * 
     * @param id 用户ID
     * @return 用户对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建新用户
     * 
     * @param user 用户对象
     * @return 创建的用户对象
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(201).body(savedUser);
    }

    /**
     * 更新用户信息
     * 
     * @param id 用户ID
     * @param userDetails 更新的用户信息
     * @return 更新后的用户对象
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(userDetails.getUsername());
                    existingUser.setEmail(userDetails.getEmail());
                    User updatedUser = userService.saveUser(existingUser);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 无内容响应
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取用户总数
     * 
     * @return 用户总数
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        long count = userService.countUsers();
        return ResponseEntity.ok(count);
    }
}
