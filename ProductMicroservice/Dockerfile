# Stage 1: Build core library
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY core/.mvn core/.mvn
COPY core/mvnw core/mvnw
COPY core/pom.xml core/pom.xml
COPY core/src core/src
RUN cd core && chmod +x mvnw && ./mvnw clean install -DskipTests

# Stage 2: Build service
COPY ProductMicroservice/.mvn ProductMicroservice/.mvn
COPY ProductMicroservice/mvnw ProductMicroservice/mvnw
COPY ProductMicroservice/pom.xml ProductMicroservice/pom.xml
RUN cd ProductMicroservice && chmod +x mvnw && ./mvnw dependency:go-offline -DskipTests

COPY ProductMicroservice/src ProductMicroservice/src
RUN cd ProductMicroservice && ./mvnw clean package -DskipTests

# Stage 3: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/ProductMicroservice/target/*.jar app.jar
EXPOSE 20001
ENTRYPOINT ["java", "-jar", "app.jar"]
