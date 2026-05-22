#Construction de l'application'
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

#Création de l'image docker
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/backend_sondages-0.0.1-SNAPSHOT.jar ./backend_sondage.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

EXPOSE 8080
ENTRYPOINT [ "/wait-for-it.sh", "mysql-db:3306", "--", "java", "-jar", "backend_sondage.jar" ]