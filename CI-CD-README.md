# Spring Boot CI/CD 工作流使用说明

## 📋 概述

本项目包含一个完整的 GitHub Actions CI/CD 工作流，用于自动化构建、测试和部署 Spring Boot 应用。

## 🚀 工作流程

### 触发条件
- ✅ 代码推送到 `main` 分支时自动触发
- ✅ 针对 `main` 分支的 Pull Request 也会触发（仅执行测试）

### 工作流程步骤

#### 1️⃣ 构建与测试阶段（build-and-test）
- 设置 JDK 环境（默认 JDK 17）
- 启动 MySQL 8.0 服务容器
- 缓存 Maven 依赖以加速构建
- 执行单元测试和集成测试
- 上传测试结果供查看

#### 2️⃣ Docker 镜像构建阶段（docker-build）
- 仅在 `main` 分支的 push 事件时执行
- 构建 Spring Boot JAR 包
- 使用多阶段构建创建优化的 Docker 镜像
- 自动标记镜像（Git SHA、分支名、latest）
- 推送镜像到 Docker Hub（需配置密钥）

## ⚙️ 配置说明

### 环境变量
在 `.github/workflows/ci-cd.yml` 中可以修改以下变量：

```yaml
env:
  JAVA_VERSION: '17'           # JDK 版本
  MYSQL_VERSION: '8.0'         # MySQL 版本
  DOCKER_IMAGE_NAME: spring-boot-app  # Docker 镜像名称
```

### 数据库配置
工作流会自动启动 MySQL 服务，测试时可以访问：
- **Host**: `127.0.0.1`
- **Port**: `3306`
- **Database**: `test_db`
- **Username**: `test_user`
- **Password**: `test_password`

在 `application-test.properties` 或测试配置中使用：
```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/test_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=test_user
spring.datasource.password=test_password
```

### Docker Hub 配置（可选）

如需推送 Docker 镜像到 Docker Hub，需要在 GitHub 仓库中配置以下 Secrets：

1. 进入仓库 Settings → Secrets and variables → Actions
2. 添加以下 Repository secrets：
   - `DOCKERHUB_USERNAME`: 你的 Docker Hub 用户名
   - `DOCKERHUB_TOKEN`: Docker Hub Access Token（不是密码）

**获取 Docker Hub Token：**
1. 登录 Docker Hub
2. 进入 Account Settings → Security
3. 点击 "New Access Token"
4. 生成并复制 Token

## 📁 项目文件结构

```
.
├── .github/
│   └── workflows/
│       └── ci-cd.yml          # CI/CD 工作流配置
├── Dockerfile                  # Docker 镜像构建文件
├── .dockerignore              # Docker 忽略文件
├── pom.xml                    # Maven 配置文件
└── src/
    ├── main/
    └── test/
```

## 🔧 自定义配置

### 修改 JDK 版本
编辑 `.github/workflows/ci-cd.yml`：
```yaml
env:
  JAVA_VERSION: '21'  # 改为 11、17 或 21
```

### 修改 MySQL 版本
```yaml
env:
  MYSQL_VERSION: '5.7'  # 改为 5.7 或 8.0
```

### 跳过 Docker 镜像推送
如果不需要推送镜像，可以注释掉或删除 Docker Hub 登录步骤。

### 添加部署阶段
工作流中已包含部署阶段的模板，取消注释并根据需要修改：
```yaml
deploy:
  name: 部署应用
  runs-on: ubuntu-latest
  needs: docker-build
  steps:
    - name: 部署到服务器
      run: |
        # 添加你的部署脚本
        # 例如：SSH 部署、Kubernetes 部署等
```

## 🧪 本地测试

### 测试 Maven 构建
```bash
mvn clean verify
```

### 测试 Docker 构建
```bash
docker build -t spring-boot-app:local .
docker run -p 8080:8080 spring-boot-app:local
```

## 🐛 故障排查

### 常见问题

1. **MySQL 连接失败**
   - 检查工作流中的 MySQL 健康检查配置
   - 确保测试配置使用正确的数据库连接信息

2. **Maven 依赖下载慢**
   - 工作流已启用 Maven 缓存，首次构建后会加速
   - 可以考虑使用国内 Maven 镜像

3. **Docker 构建失败**
   - 检查 Dockerfile 是否正确
   - 确认 `target/*.jar` 文件存在
   - 查看构建日志中的错误信息

4. **权限问题**
   - 确保 Docker Hub Token 有正确的权限
   - 检查 GitHub Secrets 配置是否正确

## 📊 监控工作流

1. 进入 GitHub 仓库的 "Actions" 标签页
2. 查看工作流运行状态
3. 点击具体运行查看详细日志
4. 下载测试结果 artifact（如果测试失败）

## 🔐 安全建议

- ✅ 永远不要将敏感信息硬编码在工作流文件中
- ✅ 使用 GitHub Secrets 管理所有敏感数据
- ✅ 定期更新 Docker Hub Token
- ✅ 限制工作流权限（使用最小权限原则）
- ✅ 审查第三方 Actions 的安全性

## 📚 参考资料

- [GitHub Actions 文档](https://docs.github.com/en/actions)
- [Spring Boot Docker 指南](https://spring.io/guides/topicals/spring-boot-docker/)
- [Docker 最佳实践](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/)

## 💡 提示

- 工作流使用了最新的 Action 版本（v4、v5），建议定期检查更新
- 可以利用 GitHub Actions 缓存加速构建过程
- 考虑添加通知机制（如 Slack、邮件）以便及时了解构建状态
