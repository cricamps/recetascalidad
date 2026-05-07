# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# El perfil se controla via variable de entorno SPRING_PROFILES_ACTIVE en Railway
# Si no se define, usa 'local' (H2) por defecto
ENTRYPOINT ["java", "-jar", "app.jar"]
