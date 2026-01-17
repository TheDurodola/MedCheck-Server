# Use a build stage to keep the final image small
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Final runtime stage
FROM eclipse-temurin:17-jre-jammy
COPY --from=build /target/*.jar app.jar

# This is the critical part: tell Spring to use the $PORT variable
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "/app.jar"]