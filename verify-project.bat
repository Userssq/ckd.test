@echo off
echo 验证 Spring Boot 项目结构...
echo.

echo 检查主要文件:
if exist "pom.xml" (echo ✓ pom.xml 存在) else (echo ✗ pom.xml 缺失)
if exist "src\main\java\com\ckd\test\CkdTestApplication.java" (echo ✓ 启动类存在) else (echo ✗ 启动类缺失)
if exist "src\main\resources\application.yml" (echo ✓ 主配置文件存在) else (echo ✗ 主配置文件缺失)
if exist "src\test\java\com\ckd\test\UserServiceTest.java" (echo ✓ 单元测试存在) else (echo ✗ 单元测试缺失)
if exist "src\test\java\com\ckd\test\UserControllerIntegrationTest.java" (echo ✓ 集成测试存在) else (echo ✗ 集成测试缺失)
if exist ".github\workflows\ci-cd.yml" (echo ✓ CI/CD 配置存在) else (echo ✗ CI/CD 配置缺失)
if exist "Dockerfile" (echo ✓ Dockerfile 存在) else (echo ✗ Dockerfile 缺失)

echo.
echo 检查目录结构:
if exist "src\main\java\com\ckd\test\controller" (echo ✓ controller 目录存在) else (echo ✗ controller 目录缺失)
if exist "src\main\java\com\ckd\test\entity" (echo ✓ entity 目录存在) else (echo ✗ entity 目录缺失)
if exist "src\main\java\com\ckd\test\repository" (echo ✓ repository 目录存在) else (echo ✗ repository 目录缺失)
if exist "src\main\java\com\ckd\test\service" (echo ✓ service 目录存在) else (echo ✗ service 目录缺失)

echo.
echo 项目结构验证完成！
