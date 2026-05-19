# Backend Sondages

Backend de l'application **Sondages App** — API REST pour la gestion de sondages en ligne.

## Stack technique

- **Java 17**
- **Spring Boot 3.5**
- **Spring Security + JWT** (authentification avec jjwt)
- **Spring Data JPA / Hibernate** (ORM)
- **Spring Validation** (validation des entrées)
- **MySQL** (base de données)
- **Lombok** (réduction du boilerplate)
- **Swagger / OpenAPI** (documentation API)
- **Maven** (gestion de dépendances)

## Prérequis

- Java 17+
- Maven 3.9+
- MySQL 8+

## Installation

1. Cloner le repo :

```bash
git clone https://github.com/loictchinda/backend_sondages.git
cd backend_sondages
```

2. Créer la base de données MySQL :

```sql
CREATE DATABASE sondages_db;
```

3. Configurer la connexion dans `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sondages_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

## Lancement

```bash
./mvnw spring-boot:run
```

Le serveur démarre sur `http://localhost:8080/api/v1`

## Documentation API (Swagger)

Une fois le serveur lancé, accéder à :

```
http://localhost:8080/api/v1/swagger-ui/index.html
```

## Structure du projet

```
src/main/java/com/example/backend_sondages/
├── config/          # Configuration (Security, CORS, JWT)
├── controller/      # Endpoints REST
├── dto/             # Objets de transfert (request/response)
├── entity/          # Entités JPA
├── repository/      # Repositories Spring Data
├── service/         # Logique métier
├── security/        # Filtres JWT, UserDetailsService
└── exception/       # Gestion globale des erreurs
```

## Base de données

Le script SQL de création de la base est disponible dans `sondages_db.sql`.

5 tables : `utilisateur`, `sondage`, `option_reponse`, `vote`, `invitation`.

## Répartition Backend

| Développeur | Domaine |
|-------------|---------|
| Dev 1       | Auth & Users |
| Dev 2       | Sondages & Options |
| Dev 3       | Votes & Résultats |

## Repo associé

- [Frontend Sondages](https://github.com/loictchinda/sondages_app)

## Statut

Projet en cours de développement