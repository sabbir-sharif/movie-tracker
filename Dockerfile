# Use lightweight Java image
FROM eclipse-temurin:17-jdk-alpine

# App name
ARG JAR_FILE=target/movie-tracker-0.0.1-SNAPSHOT.jar

# Copy jar into container
COPY ${JAR_FILE} app.jar

# Run app
ENTRYPOINT ["java","-jar","/app.jar"]