# ===== Etapa 1: compilar =====
# Imagen con Maven y JDK 21: trae todo lo necesario para compilar, pero pesa mucho
FROM maven:3.9-eclipse-temurin-21 AS compilacion
WORKDIR /app

# Primero solo el pom.xml: si no cambian las dependencias, Docker reutiliza esta capa de la caché
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Después el código: es lo que cambia a menudo, así solo se repite desde aquí
COPY src ./src
# Los tests se pasan fuera (mvnw test); aquí solo empaquetamos el .jar
RUN mvn -B -q package -DskipTests

# ===== Etapa 2: ejecutar =====
# Solo JRE 21 sobre Alpine: lo justo para ejecutar el .jar, la imagen final es mucho más ligera
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Usuario sin privilegios: si alguien compromete la app, no es root dentro del contenedor
RUN addgroup -S muro && adduser -S muro -G muro

# De la etapa 1 solo nos llevamos el .jar; Maven y el código fuente se quedan fuera
COPY --from=compilacion /app/target/muro-notas.jar app.jar

USER muro

# Puerto de Spring Boot dentro del contenedor (documentativo; no se publica al exterior)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]