# Utilizar una imagen base de OpenJDK que coincida con tu versión de Java
FROM openjdk:17-jdk-slim as build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo de construcción de Gradle en el contenedor
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Copiar el código fuente
COPY src src

# Conceder permisos de ejecución al script de Gradle
RUN chmod +x ./gradlew

# Compilar el proyecto y saltar los tests
RUN ./gradlew build -x test

# Ejecutar la aplicación con una imagen base de Java más ligera
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el JAR compilado desde el paso de construcción
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer el puerto en el que se ejecutará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
