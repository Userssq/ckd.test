# ============================================
# Spring Boot 应用 Dockerfile
# 多阶段构建以优化镜像大小
# ============================================

# 第一阶段：构建阶段
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 复制 pom.xml 并下载依赖（利用 Docker 缓存层）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码并构建
COPY src ./src
RUN mvn clean package -DskipTests -B

# 第二阶段：运行阶段
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 添加元数据标签
LABEL maintainer="devops-team"
LABEL description="Spring Boot Application"

# 创建非 root 用户以提高安全性
RUN addgroup -S spring && adduser -S spring -G spring

# 从构建阶段复制 JAR 包
COPY --from=build /app/target/*.jar app.jar

# 修改文件所有权
RUN chown -R spring:spring /app

# 切换到非 root 用户
USER spring

# 暴露应用端口（根据 application.properties 配置修改）
EXPOSE 8080

# JVM 参数优化
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
