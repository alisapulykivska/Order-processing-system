# Stage 1: Build core library
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY core/.mvn core/.mvn
COPY core/mvnw core/mvnw
COPY core/pom.xml core/pom.xml
COPY core/src core/src
RUN cd core && chmod +x mvnw && ./mvnw clean install -DskipTests

# Stage 2: Build service
COPY NotificationMicroservice/.mvn NotificationMicroservice/.mvn
COPY NotificationMicroservice/mvnw NotificationMicroservice/mvnw
COPY NotificationMicroservice/pom.xml NotificationMicroservice/pom.xml
RUN cd NotificationMicroservice && chmod +x mvnw && ./mvnw dependency:go-offline -DskipTests

COPY NotificationMicroservice/src NotificationMicroservice/src
RUN cd NotificationMicroservice && ./mvnw clean package -DskipTests

# Stage 3: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/NotificationMicroservice/target/*.jar app.jar
EXPOSE 20004
ENTRYPOINT ["java", "-jar", "app.jar"]
