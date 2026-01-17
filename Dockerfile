# Use JDK 21 for the build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Use JRE 21 for the final runtime stage
FROM eclipse-temurin:21-jre-jammy
COPY --from=build /target/*.jar app.jar

# Render provides the PORT environment variable automatically
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "/app.jar"]