# 第一阶段：构建器阶段
FROM openjdk:8 as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# 第二阶段：运行时阶段
FROM openjdk:8
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
RUN ls -l
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
