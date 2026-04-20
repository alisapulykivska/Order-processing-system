# Stage 1: Build core library
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY core/.mvn core/.mvn
COPY core/mvnw core/mvnw
COPY core/pom.xml core/pom.xml
COPY core/src core/src
RUN cd core && chmod +x mvnw && ./mvnw clean install -DskipTests

# Stage 2: Build service
COPY OrderMicroservice/.mvn OrderMicroservice/.mvn
COPY OrderMicroservice/mvnw OrderMicroservice/mvnw
COPY OrderMicroservice/pom.xml OrderMicroservice/pom.xml
RUN cd OrderMicroservice && chmod +x mvnw && ./mvnw dependency:go-offline -DskipTests

COPY OrderMicroservice/src OrderMicroservice/src
RUN cd OrderMicroservice && ./mvnw clean package -DskipTests

# Stage 3: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/OrderMicroservice/target/*.jar app.jar
EXPOSE 20002
ENTRYPOINT ["java", "-jar", "app.jar"]
