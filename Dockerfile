FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Pprod -DskipTests

# Debug: Periksa dependency tree
RUN mvn dependency:tree | grep swagger || echo "Swagger dependency tidak ditemukan"

# Debug: Periksa isi JAR
RUN jar tf target/*.jar | grep swagger-ui | head -10 || echo "Swagger UI resources tidak ditemukan"

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/cms-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
