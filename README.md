# Backend Sondages - Sondages App 📊

Backend de l'application **Sondages App** — API REST pour la gestion de sondages en ligne.

## 🛠️ Stack technique

- **Java 17**
- **Spring Boot 3.4.2**
- **Spring Security + JWT** (authentification avec `jjwt` 0.12.5)
- **Spring Data JPA / Hibernate** (ORM)
- **Spring Validation** (validation des entrées)
- **MySQL 8+** (base de données)
- **Lombok** (réduction du boilerplate)
- **Swagger / OpenAPI 3** (documentation API)
- **Maven** (gestion de dépendances)

## ✅ Fonctionnalités Implémentées (Validation Rendu n°2)

### 1. Base de données & Modèles (JPA)
- Création des entités synchronisées avec le script `sondages_db.sql`.
- Tables mappées : `Utilisateur`, `Sondage`, `OptionReponse`, `Vote` (avec contrainte anti-double vote), et `Invitation` (Clé composite).

### 2. MVP : Authentification Simple (Dev 1)
- **Inscription (`POST /api/v1/auth/register`)** : Création d'un utilisateur avec validation des champs, vérification d'unicité (pseudo/email) et hashage du mot de passe (BCrypt).
- **Connexion (`POST /api/v1/auth/login`)** : Authentification et génération d'un token JWT sécurisé valide pour les requêtes futures.
- *[Insérer ici un Screenshot de Swagger ou Postman montrant un retour 200 OK pour l'inscription]*
- *[Insérer ici un Screenshot de Swagger ou Postman montrant le Token JWT généré lors de la connexion]*

## 🚀 Prérequis et Installation

### Prérequis
- Java 17+
- Maven 3.9+
- MySQL 8+

### Étapes de lancement

1. **Cloner le repo :**
```bash
git clone https://github.com/loictchinda/backend_sondages.git
cd backend_sondages
```

2. **Créer la base de données MySQL locale :**
```sql
CREATE DATABASE IF NOT EXISTS sondages_db;
```

3. **Lancement de l'application :**
À la racine du projet, lancez la commande Maven :
```bash
./mvnw clean spring-boot:run
```
*(Sur Windows classique, utilisez `mvnw clean spring-boot:run`)*

Le serveur démarre sur `http://localhost:8080/api/v1`

## 📖 Documentation API (Swagger)

Une fois le serveur lancé, accéder à :

👉 **[http://localhost:8080/api/v1/swagger-ui/index.html](http://localhost:8080/api/v1/swagger-ui/index.html)**

## 📂 Structure du projet

```text
src/main/java/com/example/demo/
├── config/          # Configuration (SecurityConfig avec accès Swagger/Auth)
├── controller/      # Endpoints REST (AuthController)
├── dto/             # Objets de transfert (RegisterRequest, LoginRequest, JwtResponse)
├── entity/          # Entités JPA (Utilisateur, Sondage, Vote...)
├── repository/      # Repositories Spring Data (UtilisateurRepository...)
├── service/         # Logique métier (AuthService...)
└── security/        # Utilitaires de Sécurité (JwtUtils)
```

## 👥 Répartition Backend (Rôles)

| Développeur | Domaine | Statut |
|-------------|---------|--------|
| **Dev 1** | Auth & Users | ✅ Terminé |
| **Dev 2** | Sondages & Options | ⏳ À faire |
| **Dev 3** | Votes & Résultats | ⏳ À faire |

## 🔗 Liens associés

- [Frontend Sondages](https://github.com/loictchinda/sondages_app)

## Statut

Projet en cours de développement