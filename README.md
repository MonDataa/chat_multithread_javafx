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

## Captures d'écran

Voici un aperçu de l'application de chat en action:

![Capture d'écran de l'Application de Chat](C:\Users\ASUS\Documents\Programme\M1TNSID\s7\Architecture\java\td\chat_multithread_javafx\client_chat_screen_shot.png)
