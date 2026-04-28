# Stage 1: Build core library
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY core/.mvn core/.mvn
COPY core/mvnw core/mvnw
COPY core/pom.xml core/pom.xml
COPY core/src core/src
RUN cd core && chmod +x mvnw && ./mvnw clean install -DskipTests

# Stage 2: Build service
COPY UserMicroservice/.mvn UserMicroservice/.mvn
COPY UserMicroservice/mvnw UserMicroservice/mvnw
COPY UserMicroservice/pom.xml UserMicroservice/pom.xml
RUN cd UserMicroservice && chmod +x mvnw && ./mvnw dependency:go-offline -DskipTests

COPY UserMicroservice/src UserMicroservice/src
RUN cd UserMicroservice && ./mvnw clean package -DskipTests

# Stage 3: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/UserMicroservice/target/*.jar app.jar
EXPOSE 20000
ENTRYPOINT ["java", "-jar", "app.jar"]
