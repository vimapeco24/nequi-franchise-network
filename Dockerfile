# Stage of building the image
FROM gradle:8.5-jdk21 as builder
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN ./gradlew build --no-daemon

# Stage of running the image
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]