# Stage 1: Build core library
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY core/.mvn core/.mvn
COPY core/mvnw core/mvnw
COPY core/pom.xml core/pom.xml
COPY core/src core/src
RUN cd core && chmod +x mvnw && ./mvnw clean install -DskipTests

# Stage 2: Build service
COPY PaymentMicroservice/.mvn PaymentMicroservice/.mvn
COPY PaymentMicroservice/mvnw PaymentMicroservice/mvnw
COPY PaymentMicroservice/pom.xml PaymentMicroservice/pom.xml
RUN cd PaymentMicroservice && chmod +x mvnw && ./mvnw dependency:go-offline -DskipTests

COPY PaymentMicroservice/src PaymentMicroservice/src
RUN cd PaymentMicroservice && ./mvnw clean package -DskipTests

# Stage 3: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/PaymentMicroservice/target/*.jar app.jar
EXPOSE 20003
ENTRYPOINT ["java", "-jar", "app.jar"]
