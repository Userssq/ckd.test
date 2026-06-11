package com.ckd.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.ckd.test.entity.User;
import com.ckd.test.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用户控制器集成测试类
 * 
 * @author ckd
 * @version 1.0.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 清理测试数据
        userRepository.deleteAll();
        
        // 创建测试用户
        testUser = new User("integrationtest", "integration@example.com");
    }

    @Test
    void testGetAllUsers() throws Exception {
        // 保存测试用户
        userRepository.save(testUser);

        // 发送GET请求获取所有用户
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].username").value("integrationtest"));
    }

    @Test
    void testGetUserById() throws Exception {
        // 保存测试用户
        User savedUser = userRepository.save(testUser);

        // 发送GET请求获取指定ID的用户
        mockMvc.perform(get("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("integrationtest"))
                .andExpect(jsonPath("$.email").value("integration@example.com"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        // 发送GET请求获取不存在的用户
        mockMvc.perform(get("/api/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        // 创建新用户对象
        User newUser = new User("newuser", "newuser@example.com");

        // 发送POST请求创建用户
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        // 保存测试用户
        User savedUser = userRepository.save(testUser);

        // 更新用户信息
        savedUser.setUsername("updateduser");
        savedUser.setEmail("updated@example.com");

        // 发送PUT请求更新用户
        mockMvc.perform(put("/api/users/{id}", savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        // 保存测试用户
        User savedUser = userRepository.save(testUser);

        // 发送DELETE请求删除用户
        mockMvc.perform(delete("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isNoContent());

        // 验证用户已被删除
        mockMvc.perform(get("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserCount() throws Exception {
        // 保存多个用户
        userRepository.save(new User("user1", "user1@example.com"));
        userRepository.save(new User("user2", "user2@example.com"));

        // 发送GET请求获取用户数量
        mockMvc.perform(get("/api/users/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }
}
