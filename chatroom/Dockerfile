# 使用OpenJDK基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制Maven构建文件
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# 下载依赖（缓存层）
RUN ./mvnw dependency:go-offline -B

# 复制项目源代码
COPY src src

# 构建项目
RUN ./mvnw package -DskipTests

# 删除不必要的文件
RUN rm -rf target/*.class \
    && rm -rf .mvn/wrapper \
    && rm -rf ~/.m2/repository

# 复制生成的JAR文件
COPY target/*.jar app.jar

# 暴露Spring Boot应用端口
EXPOSE 8080

# 启动应用
CMD ["java", "-jar", "app.jar"]