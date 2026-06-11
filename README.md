# CKD Test Spring Boot 项目

这是一个基于 Spring Boot 3.2.x 的示例项目，专为 CI/CD 流水线设计。

## 项目结构

```
ckd.test/
├── .github/workflows/ci-cd.yml    # GitHub Actions CI/CD 配置
├── src/
│   ├── main/
│   │   ├── java/com/ckd/test/
│   │   │   ├── CkdTestApplication.java     # 应用启动类
│   │   │   ├── controller/
│   │   │   │   └── UserController.java     # 用户控制器
│   │   │   ├── entity/
│   │   │   │   └── User.java              # 用户实体类
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java    # 用户数据访问接口
│   │   │   └── service/
│   │   │       └── UserService.java       # 用户服务类
│   │   └── resources/
│   │       └── application.yml            # 主配置文件
│   └── test/
│       ├── java/com/ckd/test/
│       │   ├── UserServiceTest.java       # 服务层单元测试
│       │   └── UserControllerIntegrationTest.java  # 控制器集成测试
│       └── resources/
│           ├── application.yml            # 测试配置文件
│           └── application-test.yml       # 测试环境配置
├── pom.xml                                # Maven 配置文件
├── Dockerfile                             # Docker 镜像构建文件
└── README.md                              # 项目说明文档
```

## 技术栈

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Maven**: 3.9.6
- **Database**: MySQL 8.0 (生产), H2 (测试)
- **ORM**: Spring Data JPA
- **Testing**: JUnit 5, Spring Boot Test

## 快速开始

### 本地运行

```bash
# 克隆项目
git clone <repository-url>
cd ckd.test

# 编译项目
mvn clean compile

# 运行测试
mvn test

# 启动应用
mvn spring-boot:run
```

### 环境变量配置

应用支持以下环境变量来配置数据库连接：

- `MYSQL_HOST`: MySQL 主机地址 (默认: localhost)
- `MYSQL_PORT`: MySQL 端口 (默认: 3306)
- `MYSQL_DATABASE`: 数据库名称 (默认: test_db)
- `MYSQL_USERNAME`: 数据库用户名 (默认: test_user)
- `MYSQL_PASSWORD`: 数据库密码 (默认: test_password)

## API 端点

- `GET /api/users` - 获取所有用户
- `GET /api/users/{id}` - 根据ID获取用户
- `POST /api/users` - 创建新用户
- `PUT /api/users/{id}` - 更新用户信息
- `DELETE /api/users/{id}` - 删除用户
- `GET /api/users/count` - 获取用户总数

## CI/CD 流水线

项目配置了完整的 GitHub Actions CI/CD 流水线：

1. **触发条件**: 推送到 main 分支
2. **构建与测试**: 
   - 设置 JDK 17 环境
   - 启动 MySQL 服务容器
   - 执行单元测试
3. **Docker 构建**: 
   - 构建 Docker 镜像
   - 推送到 Docker Hub (需配置 secrets)

## 测试

项目包含完整的测试套件：

- **单元测试**: `UserServiceTest.java` - 测试服务层逻辑
- **集成测试**: `UserControllerIntegrationTest.java` - 测试 REST API 端点

运行测试：
```bash
mvn test
```

## Docker 部署

```bash
# 构建镜像
docker build -t ckd-test .

# 运行容器
docker run -p 8080:8080 ckd-test
```

## 健康检查

应用集成了 Spring Boot Actuator，提供健康检查端点：

- `GET /actuator/health` - 应用健康状态
- `GET /actuator/info` - 应用信息

## 许可证

本项目仅供学习和演示使用。