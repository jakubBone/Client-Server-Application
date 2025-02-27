# Image for Java21
FROM eclipse-temurin:21-jdk

# Working directory setting
WORKDIR /app

# Copy files
COPY build/libs/ServerLauncher.jar ServerLauncher.jar
COPY src/main/resources/ /app/

# Application port
EXPOSE 5000

# Run the application
CMD ["java","-jar", "ServerLauncher.jar"]


