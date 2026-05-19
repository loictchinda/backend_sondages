# Backend Sondages

Backend de l'application **Sondages App** — API REST pour la gestion de sondages en ligne.

## Stack technique

- **Java 17**
- **Spring Boot 4.0.6**
- **Spring Security** (authentification JWT)
- **Spring Data JPA** (accès base de données)
- **Spring Validation** (validation des entrées)
- **Spring WebMVC** (API REST)
- **MySQL** (base de données)
- **Lombok** (réduction du boilerplate)
- **Maven** (gestion de dépendances)

## Prérequis

- Java 17+
- Maven 3.9+
- MySQL 8+

## Installation

```bash
git clone https://github.com/loictchinda/backend_sondages.git
cd backend_sondages
```

Créer la base de données MySQL :

```sql
CREATE DATABASE sondages_db;
```

Configurer la connexion dans `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sondages_db
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Lancement

```bash
./mvnw spring-boot:run
```

Le serveur démarre sur `http://localhost:8080`

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

## Répartition Backend

| Développeur | Domaine |
|-------------|---------|
| Dev 1       | Auth & Users |
| Dev 2       | Sondages & Options |
| Dev 3       | Votes & Résultats |

## Repo associé

- [Frontend Sondages](https://github.com/loictchinda/sondages_app)

## Statut

🚧 Projet en cours de développement
