
# Multi-stage build optimizado para Railway
FROM maven:3.9-openjdk-21-slim AS builder

# Crear usuario no-root para seguridad
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

WORKDIR /app

# Copiar solo pom.xml primero para aprovechar cache de Docker
COPY pom.xml .

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -B

# Etapa final: Runtime
FROM openjdk:21-jre-slim

# Instalar curl para health checks
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Crear usuario no-root
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

WORKDIR /app

# Copiar JAR desde la etapa de build
COPY --from=builder /app/target/biblioteca-1.0-SNAPSHOT.jar app.jar

# Cambiar propietario y permisos
RUN chown -R appuser:appgroup /app
USER appuser

# Variables de entorno para Railway
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SERVER_PORT=8080

# Puerto que expone la aplicación
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Comando de inicio optimizado
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar --server.port=$PORT"]"]