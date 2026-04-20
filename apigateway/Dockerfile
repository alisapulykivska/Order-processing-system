FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY apigateway/.mvn .mvn
COPY apigateway/mvnw mvnw
COPY apigateway/pom.xml pom.xml
RUN chmod +x mvnw && ./mvnw dependency:go-offline -DskipTests

COPY apigateway/src src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
