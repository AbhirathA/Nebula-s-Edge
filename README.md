# Nebula's Edge: Space Invader Game  

## Overview  
**Nebula's Edge** is a modern take on the classic 2D **Space Invader** genre. Players engage in wave-based space shooter mechanics, enjoy boss fights, and participate in multiplayer modes with secure account handling and real-time networking.  

---

## Features  
- **Single-Player Mode**: Fight waves of alien invaders and defeat bosses.  
- **Multiplayer Modes**: Co-op and versus modes with real-time communication.  
- **Enemy Boss Fights**: Unique attack patterns and challenging gameplay.  
- **Secure Account Handling**: Login using Firebase Authentication.  
- **Real-Time Database**: Track user progress and multiplayer data.  
- **Physics Engine**: C++-based physics for smooth gameplay.  

---

## Technical Details  

### Backend  
- **Languages**: Java (server-side logic), C++ (physics engine).  
- **Database**: Firebase Firestore and Realtime Database for data storage.  
- **Networking**: Socket programming for multiplayer synchronization.  

### Frontend  
- **Framework**: LibGDX for 2D rendering and gameplay.  
- **UI**: Intuitive design with smooth animations and HUD elements.  

---

## Development Setup  

1. Clone the repository:  
   ```bash
   git clone https://github.com/AbhirathA/Nebula-s-Edge
   cd Nebula-s-Edge
   make
   ```
2. Build server and client packages using Gradle scripts.
3. Run the server and client applications.

## Libraries Used
- LibGDX: Frontend 2D rendering.
- Firebase: Authentication and database management.
- SFML: Backend testing for the physics engine.
  Swing: Backend testing for the physics engine.
- Logback: Logging framework for debugging.
- Gson: JSON serialization/deserialization.
- Pixelorama: Retro-style sprite design.

## Documentation
- Client Javadocs: https://abhiratha.github.io/Nebula-s-Edge/ClientJavadoc/
- Server Javadocs: https://abhiratha.github.io/Nebula-s-Edge/ServerJavadoc
