# --- ETAPA 1: Compilación (Build) ---
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar el pom.xml y descargar dependencias (esto optimiza el cache de Docker)
COPY fondos/pom.xml .
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar el proyecto
COPY fondos/src ./src
RUN mvn clean package -DskipTests

# --- ETAPA 2: Imagen Final (Runtime) ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copiar solo el archivo JAR desde la etapa anterior
# Nota: Asegúrate de que el nombre coincida con el de tu pom.xml
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]