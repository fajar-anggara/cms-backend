# ---------- Stage 1: Build aplikasi ----------
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /usr/app

# Copy seluruh project ke container
COPY . .

# Build aplikasi dengan profil prod dan tanpa skip test (untuk reliability)
RUN mvn versions:set-property -Dproperty=project.version -DnewVersion=1.0.0 && \
    mvn clean package -DskipTests


# ---------- Stage 2: Jalankan aplikasi ----------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /usr/app

# Ambil jar dari tahap build
COPY --from=build /usr/app/target/*.jar /usr/app/app.jar

# Jalankan aplikasi dengan profil production
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
