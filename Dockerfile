# =========================================================
# ETAPA 1: Build (Compilação)
# =========================================================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia o pom.xml e baixa as dependências (otimiza o cache do Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte e gera o .jar
COPY src ./src
RUN mvn clean package -DskipTests

# =========================================================
# ETAPA 2: Runtime (Execução)
# =========================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia apenas o .jar gerado na etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]