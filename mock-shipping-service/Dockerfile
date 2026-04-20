FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY mock-shipping-service/.mvn .mvn
COPY mock-shipping-service/mvnw mvnw
COPY mock-shipping-service/pom.xml pom.xml
RUN chmod +x mvnw && ./mvnw dependency:go-offline -DskipTests

COPY mock-shipping-service/src src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "app.jar"]
