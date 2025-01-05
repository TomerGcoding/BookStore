# Use the official OpenJDK 21 image as the base image
FROM openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

# Specify the command to run the JAR file
CMD ["java", "-jar", "demo.jar"]