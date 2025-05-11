# 🎮 Prestewi - Application de Gestion de Boutique de Jeux Vidéo

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange?logo=mysql)
![Swing](https://img.shields.io/badge/Java%20Swing-GUI-yellowgreen)

**Prestewi** est une application de bureau développée en Java pour la gestion complète d’une boutique de jeux vidéo. Elle propose des fonctionnalités distinctes pour les **administrateurs** et les **clients**, en intégrant une base de données MySQL, une interface graphique Swing, et un système de reçus générés automatiquement lors des achats.

---

## 🧩 Fonctionnalités Principales

### 👨‍💼 Panneau Administrateur

- **Gestion des jeux** : Ajouter, modifier, supprimer et visualiser les jeux disponibles dans la boutique.
- **Gestion des utilisateurs** : Ajouter de nouveaux comptes clients, modifier les informations des utilisateurs ou supprimer des comptes.

### 👤 Interface Client

- **Navigation dans les jeux** : Rechercher et trier les jeux selon plusieurs critères (titre, prix, genre...).
- **Panier d’achat** : Ajouter des jeux au panier, consulter le contenu du panier, supprimer des articles et passer à l’achat.
- **Historique des achats** : Visualiser tous les achats précédents et les reçus associés.

---

## 💳 Paiement et Reçus

Lorsqu’un client procède au paiement :

- Un **reçu est généré automatiquement** au format `.txt`
- Il est enregistré dans un dossier spécifique sur le bureau du client
- Il contient un résumé de l’achat : jeux achetés, prix, montant total

---

## 🛠️ Technologies Utilisées

| Technologie | Description |
|-------------|-------------|
| **Java 17** | Langage principal pour la logique métier |
| **Java Swing** | Création de l'interface graphique utilisateur |
| **MySQL 8.0** | Base de données pour les utilisateurs, jeux, paniers et commandes |

---

## 🧭 Comment Fonctionne l’Application

### 🔐 Connexion

- Les utilisateurs doivent s’authentifier pour accéder à l’application.
- Les administrateurs bénéficient d’un accès privilégié au panneau de gestion.
- Les clients accèdent à la boutique et à leurs propres données uniquement.

### 🔄 Cycle d’utilisation

#### 👨‍💻 Administrateurs
1. Connexion via interface admin
2. Ajout/modification/suppression de jeux
3. Gestion des comptes clients

#### 🛍️ Clients
1. Connexion ou inscription via l’interface principale
2. Navigation et ajout de jeux au panier
3. Paiement et génération du reçu
4. Consultation de l’historique des achats

---

## 🗃️ Base de Données

La base de données **MySQL** stocke les données suivantes :
- Utilisateurs (clients & administrateurs)
- Jeux disponibles
- Paniers et commandes
- Historique des achats

---

## 🖼️ Capture d’Écran

![Capture d'écran](https://github.com/user-attachments/assets/81def0e1-8926-44da-a86a-c3b484d94a13)

---

## ✅ comment améliorer 
- Fonction de remboursement
- Statistiques de ventes pour les admins
- Interface modernisée avec JavaFX

---


