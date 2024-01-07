# Application de Chat Java

Ce projet est une application de chat implémentée en Java, utilisant JavaFX pour le client et un serveur multi-thread pour gérer les connexions des clients.

## Aperçu des Composants

### ClientApplication_v3.java (Application Client)

#### Importations
- Utilise JavaFX pour l'interface utilisateur.
- Composants tels que `Button`, `Label`, `ListView`, etc.

#### Structure
- Classe principale étendant `Application` de JavaFX.
- Gère une `Stage` (fenêtre) avec des éléments d'interface utilisateur.
- Gère les interactions utilisateur.

#### Connexion Réseau
- Gère la connexion au serveur de chat via des sockets.
- Envoie et reçoit des messages du serveur de chat.

#### Mise à Jour de l'Interface Utilisateur
- Utilise `Platform.runLater` pour les mises à jour de l'interface utilisateur depuis des threads non-JavaFX.
- Garantit que les modifications de l'interface utilisateur sont sûres pour les threads.

#### Classe `ClientSocketHandler`
- Encapsule la gestion de la connexion réseau, l'envoi et la réception des messages.

### multithreadchatserver_v3.java (Serveur Chat)

#### Importations
- Utilise des classes Java pour la gestion des entrées/sorties et des réseaux.

#### Structure
- Classe principale étendant `Thread`.
- Gère les connexions des clients.
- Maintient une liste de conversations (`conversationList`).
- Gère les threads pour chaque conversation.

## Étapes d'Implémentation

1. **Création du Serveur**
   - Développement d'un serveur multi-thread.

2. **Développement de l'Interface Client**
   - Construction de l'interface utilisateur avec JavaFX.

3. **Connexion Client-Serveur**
   - Établissement de la communication entre client et serveur.

4. **Gestion des Conversations**
   - Le serveur traite chaque conversation dans un thread distinct.


## Serveur de Chat Multithread - multithreadchatserver_v3.java

Cette partie donne un aperçu détaillé de la classe `multithreadchatserver_v3.java`, qui est au cœur du serveur de notre application de chat.

### Attributs Principaux

- `conversationList`: Gère les instances actives des conversations avec les clients.
- `nomclient`: Stocke le nom associé à chaque client connecté pour une gestion personnalisée.

### Méthodes Principales

- `main()`: Le point d'entrée pour démarrer le serveur. Initialise et démarre une instance de `multithreadchatserver_v3`.
- `run()`: Définit la logique du thread du serveur, en charge d'initialiser `ServerSocket` et de gérer les connexions entrantes.

### Classes Associées

- `Conversation` (ou `ClientHandler`): Gère la communication bidirectionnelle avec le client.
- `ServerSocket`: Attend les connexions entrantes et crée un nouveau `Socket` pour chaque client.
- `Socket`: Facilite la communication réseau entre le serveur et le client.

### Logique de la Classe Serveur

1. **Initialisation**: Le serveur démarre et initialise le `ServerSocket` sur un port spécifié.
2. **Attente des Clients**: Une boucle infinie écoute les tentatives de connexion des clients.
3. **Gestion des Connexions**: À chaque connexion réussie, un nouveau `Socket` est créé pour le client.
4. **Thread de Conversation**: Un nouveau thread est démarré pour chaque `Socket` client via une instance de la classe de gestion de conversation.
5. **Ajout à la Liste de Conversations**: Chaque nouvelle conversation est ajoutée à `conversationList`.
6. **Communication**: Les messages entrants sont lus et distribués aux autres clients, permettant un chat de groupe.


## Captures d'écran

Voici un aperçu de l'application de chat en action:

![Capture d'écran de l'Application de Chat](https://github.com/MonDataa/chat_multithread_javafx/blob/main/client_chat_screen_shot.png)
=======
Pour voir l'application en action, exécutez l'application sur votre système (`IntelliJ IDEA`).
