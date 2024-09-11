## Dockerfile Explanation

This Dockerfile sets up a Docker image for a Spring Boot project using Maven and JDK 21.

### Dockerfile Stages

1. **Build Stage** (Uses Maven to compile the project):
    ```Dockerfile
    FROM maven:3.9-ibm-semeru-21-jammy AS build
    ```
    - Uses the official Maven image with IBM Semeru JDK 21.

    ```Dockerfile
    WORKDIR /app
    ```
    - Sets the working directory in the container to `/app`.

    ```Dockerfile
    COPY pom.xml .
    ```
    - Copies the `pom.xml` file to the working directory in the container. This helps in caching dependencies.

    ```Dockerfile
    COPY src ./src
    ```
    - Copies the `src` directory containing the source code to the working directory in the container.

    ```Dockerfile
    RUN mvn clean package -DskipTests
    ```
    - Runs Maven to package the application, skipping tests (`-DskipTests`). The result will be a JAR file in the `target` directory.

2. **Runtime Stage** (Runs the built JAR file):
    ```Dockerfile
    FROM ibm-semeru-runtimes:open-21-jdk-jammy
    ```
    - Uses the IBM Semeru runtime image with JDK 21 for running the application.

    ```Dockerfile
    WORKDIR /app
    ```
    - Sets the working directory in the container to `/app`.

    ```Dockerfile
    COPY --from=build /app/target/*.jar app.jar
    ```
    - Copies the JAR file from the build stage to the runtime stage.

    ```Dockerfile
    EXPOSE 8080
    ```
    - Exposes port 8080, where the Spring Boot application will be running.

    ```Dockerfile
    ENTRYPOINT ["java", "-jar", "app.jar"]
    ```
    - Sets the entry point to run the Java application.

## Commands to Build and Run the Docker Image

### Build the Image

To build the Docker image, run the following command in the directory where your Dockerfile is located:

```bash
docker build -t my-spring-boot-app .
```

This command:
- Creates a Docker image named `my-spring-boot-app`.

### Run the Container

To run the container and expose port 8080 to the host, execute the following command:

```bash
docker run -p 8080:8080 my-spring-boot-app
```

This command:
- Starts a container from the `my-spring-boot-app` image.
- Maps port 8080 on the container to port 8080 on the host, making the application accessible at `http://localhost:8080`.

### Stop the Container

To stop a running container, you can list active containers and stop the desired one:

```bash
docker ps
docker stop <CONTAINER_ID>
```

Replace `<CONTAINER_ID>` with the ID of the container you wish to stop.