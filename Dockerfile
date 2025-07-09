# --- ESTÁGIO 1: Builder com Maven e JDK 21 ---
# Alteramos a imagem para uma que contém Maven e o JDK 21.
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


# --- ESTÁGIO 2: Imagem Final de Execução com JRE 21 ---
# AQUI ESTÁ A MUDANÇA: Usamos a imagem base com JRE 21.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Cria um usuário e grupo não-root para segurança.
RUN addgroup -S spring && adduser -S spring -G spring

# Copia o JAR compilado do estágio 'builder'.
# Lembre-se de ajustar o nome do seu JAR.
COPY --from=builder --chown=spring:spring /app/target/bicicletario-0.0.1-SNAPSHOT.jar app.jar

# Define o usuário que vai executar a aplicação.
USER spring:spring

EXPOSE 8080

# Comando para iniciar a aplicação.
ENTRYPOINT ["java", "-jar", "app.jar"]
