FROM maven:3.9.11-eclipse-temurin-25-alpine AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests


FROM eclipse-temurin:25-jdk-alpine
LABEL name="tool-app"
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 6677
ENTRYPOINT ["java", "-jar","app.jar","--spring.profiles.active=pro"]