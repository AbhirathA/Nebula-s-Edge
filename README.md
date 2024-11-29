# Software Requirements Specification

# (SRS)

## Course: EGC 211, Programming 2

### Team Members:

### Gathik Jindal (089), Abhirath Adamane (029), Mohammed Ibrahim (112),

### Aryan Vivek Bansal (513), Jayant Sharma (523), Avancha Dedeepya (006)

### GitHub Repository: https://github.com/AbhirathA/Nebula-s-Edge

### Client Javadocshttps://abhiratha.github.io/Nebula-s-Edge/ClientJavadoc/

### Server Javadocshttps://abhiratha.github.io/Nebula-s-Edge/ServerJavadoc/

### November 2024

```
Abstract
This document outlines the Software Requirements Specification (SRS) for the Space Invader Game project.
The game includes classic 2D wave-based space shooter mechanics and multiplayer modes. The SRS details the
functional and non-functional requirements, technical specifications, and system objectives.
```
## 1 Introduction

### 1.1 Project Overview

This project is a Space Invader game with primarily 2D gameplay. The game includes space shooter mechanics in
which players control a spaceship to fight waves of alien invaders and experience boss fights. The game also includes
secure account handling and multiplayer networking.

### 1.2 Scope

The final product will include:

- 2D Single Player Mode: Players complete various tasks in levels with increasing difficulty by fighting through
    waves of alien invaders.
- Multiplayer Mode: Players can compete or collaborate in ”co-op” or ”versus” modes.
- Enemy Fights: Unique boss stages with special attack patterns.
- Database Management: Keep track of different players and related data.


## 2 Objectives

The main objectives for the Space Invader game are:

- Create a 2D space shooter game with smooth and intuitive controls.
- Add multiplayer support with real-time communication.
- Ensure secure account handling and data encryption.
- Create a Java wrapper class to call C++ functions using JNI
- Handle exceptions such as:
    - Network errors
    - Account related errors
    - Device related errors

## 3 System Overview

### 3.1 Technical Specifications

- Backend:Java for server-side logic, multiplayer, and database management, etc. C++ for physics engine and
    other computationally expensive tasks.
- Frontend:A 2D rendering library in Java (LibGDX) and a custom game engine for 2D gameplay.
- Middleware:Socket programming for connecting to the central server.

### 3.2 Input/Output Requirements

- Input:User keyboard/controller inputs for ship movement, shooting, and entering user data.
- Output:Rendering 2D gameplay, sound effects, and user interface feedback.

## 4 Functional Requirements

### 4.1 Detailed Features

- 2D Game-Play: Classic wave-based space shooter where the player can move around and accomplish various
    tasks.
- Multiplayer Mode: Players can compete or collaborate, with real-time synchronization.
- Enemy Fights: Enemies with special attack patterns.
- Account Handling: Users can create accounts, save progress.

### 4.2 Use Cases

- Single-player: Player starts the game and fights waves of enemies.
- Multiplayer: Two players connect online and either compete or cooperate.


## 5 Non-functional Requirements

### 5.1 Security Requirements

- Authentication via Firebase: User authentication will be handled via Firebase Authentication, ensuring that
    players can securely log in using third-party services (email/password).
- Authorization: Only authorized users will have access to certain game features, such as playing the game or
    user settings. Unauthorized access will be blocked.
- Data Integrity: User progress and scores will be reliably stored in Firebase, with automatic backups in place to
    prevent data loss.

### 5.2 Performance Requirements

- Low Latency: The game should maintain a latency under 200ms during multiplayer matches to ensure smooth
    gameplay and responsiveness.
- High Throughput: The server should be able to handle multiple simultaneous player connections without
    performance degradation.

### 5.3 Usability Requirements

- Intuitive User Interface: The game will feature an easy-to-use UI with clear, visually appealing menus and
    controls for players of all skill levels.

### 5.4 Maintainability Requirements

- Modular Codebase: The game code will be organized into modular components to make future updates and
    maintenance easier.
- Error Reporting: The game will include error logging and reporting mechanisms to help identify and resolve
    issues quickly.

## 6 Libraries Used

- LibGDX - Frontend Application- LibGDX is used to create the 2D game application because it supports
    desktop, mobile, and web platforms, making it suitable for developing rich interactive frontend applications.
    We have included features like graphics rendering, audio, input handling, and physics integration.
- SFML - Quick Backend Testing Application- SFML (Simple and Fast Multimedia Library) is an open-
    source multimedia library designed to provide an interface for developing games and interactive applications. It
    offers modules for handling graphics, audio, windowing, and networking, making it easy to create cross-platform
    applications. It is used in our project to test out the backend C++ Physics Engine Code. This code can be
    found in the ”PhysicsEngine” and ”Beeman” branches of the repository.
- Swing - Quick Backend Testing Application- Swing is a graphical user interface (GUI) toolkit for Java
    that provides a set of lightweight (platform-independent) components for building rich, interactive desktop
    applications. It is part of the Java Foundation Classes (JFC) and builds on the Abstract Window Toolkit
    (AWT), offering a more flexible and feature-rich alternative. It is used in our project to test out the backend
    C++ Physics Velocity Verlet Code (which was originally written in Java). This code can be found in the
    ”Velocity-Verlet” branch of the repository.


- Firebase Authentication - Backend User Management- Firebase Authentication is a service provided by
    Google Firebase that helps developers easily implement authentication features in their applications. It supports
    our project by allowing us to authenticate users via email/password. As a result, Firebase Authentication
    simplifies the process of managing user accounts, securing user data, and integrating with other Firebase
    services, all while providing built-in features such as email verification and password resets.
- Firebase Realtime Database - Backend Constants Management- Firebase Realtime Database is a cloud-
    hosted NoSQL database provided by Google Firebase. It allows us to store and sync data between users in real
    time. The data is stored in JSON format and can be accessed via a secure API, enabling instant updates across
    connected devices. Changes made to the database are automatically reflected in all clients, which is ideal for
    our application. It is used in our project to dynamically update the server IP address, http port, and udp port
    numbers; client side constants such as size of windows; and server side constants such as game dynamics.
- Firebase Firestore Database - Backend User Data Management- Firestore is a flexible, scalable NoSQL
    database offered by Firebase, designed for mobile, web, and server development. It organizes data into collec-
    tions and documents, making it easier to model more complex data structures. Firestore offers real-time data
    synchronization, offline support, and powerful querying capabilities, such as compound queries, pagination, and
    indexing. It also provides strong security features via Firebase Security Rules, allowing granular control over
    data access. It is used in our project to store user information such as the number of ”kills” of the user.
- Gson - JSON Management- Gson is a Java library developed by Google for converting Java objects to JSON
    and vice versa. It is lightweight, easy to use, and supports complex object serialization/deserialization. It is
    used in our application to convert JSON messages received from Firebase to String. Moreover, it is also used
    to serialize our data into JSON format so that it can be sent to the server and back.
- Logback - Logging Management- Logback is a powerful and efficient Java-based logging framework that
    offers advanced features like flexible log level management, log file rolling, and asynchronous logging. It is
    often used as a more modern and feature-rich alternative to older logging frameworks like log4j. We have used
    this logging tool in order to log server and client messages in case of any information, warnings, errors, or
    exceptions.
- Pixelorama - Sprite Generator- Pixelorama is a pixel art engine designed for creating and managing retro-
    style graphics in games. It handles rendering pixelated visuals, maintaining a consistent aesthetic, and ensuring
    smooth performance for low-resolution assets. It integrates seamlessly with other game components, allowing
    us to create visually appealing retro-style environments, characters, and UI elements with ease.

### 6.1 Performance Requirements

- Maintain 60 FPS in the 2D game play.
- Multiplayer interactions should have a response time of less than 200ms.

### 6.2 Usability

- Controls should be simple and intuitive for gameplay.

## 7 Development Setup

This development works in Ubuntu/Linux because the make file provided uses ubuntu commands.

- git clonehttps://github.com/AbhirathA/Nebula-s-Edge
- cd Nebula-s-Edge
- make

After installations are complete, build the server and client packages by building the graddle scripts. Finally, run the
server and client by running the graddle scripts.

## 8 Workflow

### 8.1 Backend

Handles multiplayer game state, player accounts, and game behavior.

### 8.2 Frontend

Renders the game world, handles user input, and provides feedback.

### 8.3 Middleware

Facilitates communication between frontend and backend for multiplayer features and account management.

## 9 Important Files & Folders

### 9.1 Client

9.1.1 com.spaceinvaders.backend

- UDPClient.java: allows the user to send and receive UDP Packets to and from the server
- com.spaceinvaders.backend.firebase: Package for managing client side communication with firebase
    - ClientFirebase.java: This class provides methods to interact with Firebase’s Authentication REST API.
    - AuthenticationManager.java: This class handles user authentication and session management.
    - HTTPRequest.java: The HTTPRequest class provides methods to send HTTP requests to a server.

9.1.2 com.spaceinvaders.frontend

- SpaceInvadersGame.java: This class is the ”main” program of the game and is called when the game runs.
- com.spaceinvaders.frontend.background: Handles background elements like visuals, animations, or effects
    seen during gameplay or menus.
- com.spaceinvaders.frontend.gameplay: Manages gameplay logic, interactions, and mechanics for the player,
    enemies, and environment.
- com.spaceinvaders.frontend.managers: Includes classes responsible for managing game states, assets,
    sounds, or other key components.
       - ScreenManager.java: It is responsible for managing and transitioning between different screens in the
          game. It’s role includes switching between screens and loading/unloading resources for screens to optimize
          performance.
       - MyAssetManager.java: MyAssetManager manages the loading, retrieval, caching, and disposal of game
          assets such as images, sounds, and fonts.
- com.spaceinvaders.frontend.screens: Defines different game screens like main menu, game over, and pause.


- com.spaceinvaders.frontend.ui: Contains user interface elements such as buttons, scoreboards, health bars,
    and HUD components.

### 9.2 Server

9.2.1 com.physics

- Manager.java: The Manager class acts as a bridge between the Java code and the native PhysicsEngine
    library. It provides methods to manage game objects, update the game state, and interact with the physics
    engine. The class uses native methods to perform physics-related operations.

9.2.2 org.spaceinvaders

- Server.java: The Server class represents a basic server that handles client requests and manages client
    connections.
- UDPServer.java: The UDPServer class represents a basic server that handles game logic and packet sending
    via UDP.
- org.spaceinvaders.firebase: This package allows the server to connect with Firebase and send/receive user
    data.
       - Firebase.java: This class facilitates communication between the server and a Firebase database. It
          provides methods to manage user state, retrieve and update stored data, and handle user authentication.
          The class supports CRUD operations and handles common exceptions related to database access.
- org.spaceinvaders.handlers: Package for handling HTTP requests by providing common functionality for
    handling POST requests, parsing JSON input, and handling exceptions.

### 9.3 C++ Backend

9.3.1 Physics Engine

- Object- This code defines a base class Obj for game objects, encapsulating shared properties (like position,
    mass, radii, state, and bounding box) and methods for managing their behavior.
- Linear- The LinearObj class is a derived class of Obj that includes properties for velocity, acceleration, and
    terminal velocities. It calculates the object’s orientation based on velocity and provides methods to update
    velocity, acceleration, and position.
- Angular- The AngleObj class, derived from Obj, models objects that move based on an angle and radial
    velocity. It includes precomputed sine (SIN) and cosine (COS) tables for efficient trigonometric calculations.
- PhysicsApproximations- We have tested Verlet, Velocity Verlet, Beeman, and Leapfrog to approximate the
    physics of the game and realized that Verlet and Velocity Verlet were the best for our game.
-

9.3.2 Game Engine

- User- The user represents the player’s controlled object in the game which interacts with other objects
- Blackhole- A blackhole is a gravitational anomaly that attracts and consumes nearby objects.
- Bullet- A bullet is a fast-moving projectile fired by the user or enemies.


- Meteor- A meteor is a small celestial body that travels through space causing damage or destruction.
- Flare- A flare is a temporary, bright object used for drawing attention of enemies.
- Power-Ups: Power-ups are collectible items that provide temporary enhancements or abilities, improving the
    player’s performance or survival in the game.
- JNI- JNI (Java Native Interface) is used in the code to allow Java code to interact with native C++ methods,
    enabling efficient access to performance-critical operations like physics calculations or collision detection that
    are implemented in C++.


## 10 UML Diagrams

# 10.1 Server
##![Server](https://github.com/user-attachments/assets/0f3ad24e-7878-49d1-94a6-4b814dab3e86)

### 10.2 UI Client
![UIClient](https://github.com/user-attachments/assets/5557c526-9f4d-4362-b652-03381a7dfbdf)

### 10.3 Client Backend
![ClientBackend](https://github.com/user-attachments/assets/c7ce7107-fade-47f5-8d79-b6ca795f74d1)

### 10.4 Client Gameplay
![Clientgameplay](https://github.com/user-attachments/assets/6b28fced-e4b7-4bb2-b93a-43b31f8e1181)

### 10.5 Client Managers
![ClientManagers](https://github.com/user-attachments/assets/97441a2d-e983-40a4-8552-9b719bf49ce0)

### 10.6 C++ Backend
![CppUml](https://github.com/user-attachments/assets/629cd878-8ecb-4796-87b4-f96fbfa7c4ed)

## 11 Firebase

### 11.1 Authentication
![Authentication](https://github.com/user-attachments/assets/8005c742-bc8b-45bc-b3da-b20d7157fceb)

### 11.2 Firebase Database
![Database](https://github.com/user-attachments/assets/54ead6ad-1667-4309-b79f-9057742d6410)

### 11.3 Firebase Realtime Database
![Realtime](https://github.com/user-attachments/assets/693ed7f0-b616-4f39-9c30-38fec95e5b89)

## 12 Testing & Logging

- Testing each feature independently (2D, 3D, multiplayer).
- Ensure smooth transitions between 2D and 3D gameplay.
- Multiplayer mode will be tested for latency and synchronization issues.
- Logging for debugging crashes, errors, or connectivity issues.

## 13 Conclusion

The Space Invader game will feature a combination of 2D gameplay with multiplayer support and enemy fights.
Secure account handling and multiplayer networking will ensure smooth gameplay and a varied experience.
