# Use an official Java runtime as a parent image
# tag list and other info here: https://hub.docker.com/_/openjdk/
FROM openjdk:8-jre-alpine

# Set the working directory to /app
WORKDIR /myJavaHttpServer

# Copy the current directory contents into the container at /app
COPY . /myJavaHttpServer

# Make port 8500 available to the world outside this container
EXPOSE 8500

# Run app.py when the container launches
CMD ["java", "-jar", "firstHttpServer.jar"]

