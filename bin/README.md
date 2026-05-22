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

## 👥 Répartition Backend (Rôles & Tâches)

| Développeur | Domaine | Statut |
|-------------|---------|--------|
| **Dev 1** | Auth & Users | ✅ Terminé |
| **Dev 2** | Sondages & Options | ⏳ En cours |
| **Dev 3** | Votes & Résultats | ⏳ À faire |

### 📝 Domaine 1 : Sondages & Options (La Fondation)
Ce domaine concerne la création, la consultation et la gestion du cycle de vie des sondages. Les tâches dépendent de la couche d'authentification (déjà faite) pour récupérer le créateur.

**Tâches Prioritaires (MVP) :**
* **Tâche 1.1 : Création de Sondage (Le cœur du MVP) [Priorité Haute]**
    * **Description :** Implémenter l'endpoint `POST /api/v1/sondages` permettant à un utilisateur connecté de créer un sondage avec ses options.
    * **Contenu technique :** DTO `CreateSondageRequest` (titre, description, options), Service `SondageService.creerSondage()`, et validation des inputs (minimum 2 options). Dépendance : Auth (pour récupérer l'ID du créateur via le token JWT).
* **Tâche 1.2 : Consultation d'un Sondage par Token Public (MVP + Partage) [Priorité Haute]**
    * **Description :** Implémenter l'endpoint `GET /api/v1/sondages/{tokenPublic}` pour afficher les détails d'un sondage et ses options (endpoint public).
    * **Contenu technique :** DTO `SondageResponse` (titre, description, options sans les ID internes pour la sécurité).
* **Tâche 1.3 : Liste des Sondages Publics (Feature "Recherche de sondage" - 4 pts FACILE) [Priorité Moyenne]**
    * **Description :** Implémenter `GET /api/v1/sondages` pour lister tous les sondages où `visibilite = 'public'`.
    * **Contenu technique :** Méthode Repository `findByVisibilite`, DTO liste simplifiée.

**Tâches Bonus (MID / FACILE) - Parallélisables après 1.1 :**
* **Tâche 1.4 : Suppression d'un Sondage (3 pts FACILE) [Priorité Basse]**
    * **Description :** Implémenter `DELETE /api/v1/sondages/{id}`.
    * **Contraintes :** Seul le créateur peut supprimer son sondage. Vérification des droits nécessaire dans le service.
* **Tâche 1.5 : Édition de Sondage (9 pts MID) [Priorité Basse]**
    * **Description :** Implémenter `PUT /api/v1/sondages/{id}`.
    * **Contraintes strictes :** Modifiable uniquement s'il n'y a pas encore de votes (vérifier la table `Vote`).

### 🗳️ Domaine 2 : Votes & Résultats (L'Interaction)
Ce domaine gère l'action de voter et le calcul des statistiques. Il dépend de l'existence des sondages (Tâche 1.1) mais peut être commencé en parallèle si les interfaces (signatures de méthodes) sont définies à l'avance.

**Tâches Prioritaires (MVP & MID) :**
* **Tâche 2.1 : L'Action de Voter (MVP + Anti-double vote robuste - 10+12 pts) [Priorité Haute]**
    * **Description :** Implémenter `POST /api/v1/sondages/{id}/votes`.
    * **Contenu technique :** Service `VoteService.enregistrerVote()`.
    * **Le défi :** Gérer l'exception de la base de données (la contrainte unique `uk_utilisateur_sondage`) et la transformer en un message d'erreur propre pour l'utilisateur ("Vous avez déjà voté").
* **Tâche 2.2 : Calcul des Résultats (MVP - 8 pts) [Priorité Haute]**
    * **Description :** Implémenter `GET /api/v1/sondages/{id}/resultats`.
    * **Contenu technique :** DTO `ResultatSondageResponse` (total des votes, liste des options avec leur nombre de votes et le pourcentage).

**Tâches Bonus (DIFF / FACILE) - Parallélisables après 2.1 et 2.2 :**
* **Tâche 2.3 : Historique de Vote Personnel (12 pts DIFF) [Priorité Moyenne]**
    * **Description :** Implémenter `GET /api/v1/utilisateurs/me/votes`.
    * **Contenu technique :** Récupérer l'utilisateur via le token, lister tous les sondages auxquels il a participé et l'option qu'il a choisie.
* **Tâche 2.4 : La "Multi Salle de Vote" (Invitations - 16 pts DIFF) [Priorité Basse]**
    * **Description :** Gérer la logique pour les sondages `prive`.
    * **Contenu technique :** Endpoints pour ajouter un utilisateur à l'entité `Invitation`. Bloquer l'endpoint de vote (Tâche 2.1) si le sondage est privé et que l'utilisateur n'est pas dans la table `Invitation`.

## 🔗 Liens associés

- [Frontend Sondages](https://github.com/loictchinda/sondages_app)

## Statut

Projet en cours de développement