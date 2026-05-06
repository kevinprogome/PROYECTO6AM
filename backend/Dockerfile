# Proyecto: GreenHouse Manager
# Archivo: Dockerfile
# Descripcion: Imagen Docker para backend Spring Boot.
# Autor: Equipo GreenHouse Manager
# Fecha: 2026-05-06
# Version: 1.0.0

FROM maven:3.9.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
