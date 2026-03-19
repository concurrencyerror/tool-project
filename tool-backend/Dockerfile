FROM maven:3.9.11-eclipse-temurin-25-alpine AS builder

WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -q clean package -DskipTests


FROM eclipse-temurin:25-jdk-alpine

LABEL name="tool-backend"

WORKDIR /app

RUN addgroup -S app && adduser -S app -G app

COPY --from=builder /workspace/target/*.jar /app/app.jar

ENV SPRING_PROFILES_ACTIVE=pro

EXPOSE 6677

USER app

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
