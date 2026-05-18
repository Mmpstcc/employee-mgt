# ── Stage 1: Build ──────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Run ─────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8085
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -qO- http://192.168.2.29:8085/api/employees || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
