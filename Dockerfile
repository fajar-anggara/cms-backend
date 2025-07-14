FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Pprod -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/cms-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
