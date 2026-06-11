package com.ckd.test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ckd.test.entity.User;
import com.ckd.test.repository.UserRepository;
import com.ckd.test.service.UserService;

/**
 * 用户服务单元测试类
 * 
 * @author ckd
 * @version 1.0.0
 */
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 清理测试数据
        userRepository.deleteAll();
        
        // 创建测试用户
        testUser = new User("testuser", "test@example.com");
    }

    @Test
    void testSaveUser() {
        // 保存用户
        User savedUser = userService.saveUser(testUser);
        
        // 验证保存结果
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());
    }

    @Test
    void testFindById() {
        // 保存用户
        User savedUser = userService.saveUser(testUser);
        
        // 根据ID查找用户
        Optional<User> foundUser = userService.findById(savedUser.getId());
        
        // 验证查找结果
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void testFindByIdNotFound() {
        // 查找不存在的用户
        Optional<User> foundUser = userService.findById(999L);
        
        // 验证结果为空
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindAllUsers() {
        // 保存多个用户
        userService.saveUser(new User("user1", "user1@example.com"));
        userService.saveUser(new User("user2", "user2@example.com"));
        
        // 获取所有用户
        List<User> users = userService.findAllUsers();
        
        // 验证用户数量
        assertEquals(2, users.size());
    }

    @Test
    void testFindByUsername() {
        // 保存用户
        userService.saveUser(testUser);
        
        // 根据用户名查找用户
        Optional<User> foundUser = userService.findByUsername("testuser");
        
        // 验证查找结果
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testDeleteUser() {
        // 保存用户
        User savedUser = userService.saveUser(testUser);
        
        // 删除用户
        userService.deleteUser(savedUser.getId());
        
        // 验证用户已被删除
        Optional<User> foundUser = userService.findById(savedUser.getId());
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testCountUsers() {
        // 初始数量为0
        assertEquals(0, userService.countUsers());
        
        // 保存用户
        userService.saveUser(testUser);
        
        // 验证数量增加
        assertEquals(1, userService.countUsers());
    }

    @Test
    void testUpdateUser() {
        // 保存用户
        User savedUser = userService.saveUser(testUser);
        
        // 更新用户信息
        savedUser.setUsername("updateduser");
        savedUser.setEmail("updated@example.com");
        User updatedUser = userService.saveUser(savedUser);
        
        // 验证更新结果
        assertEquals("updateduser", updatedUser.getUsername());
        assertEquals("updated@example.com", updatedUser.getEmail());
    }
}
