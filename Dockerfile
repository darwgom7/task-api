# Steps image const
# Use an OpenJDK base image that matches your Java version
FROM openjdk:17-jdk-slim as build

# Set working directory inside container
WORKDIR /app

# Copy Gradle build file to container
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Copy the source code
COPY src src

# Grant execute permissions to the Gradle script
RUN chmod +x ./gradlew

# Compile the project
RUN ./gradlew build -x test

# Run the application with a lighter Java base image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the compiled JAR from the build step
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port on which the application will run
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
