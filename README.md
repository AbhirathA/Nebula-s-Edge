# Nebula's Edge

![Java](https://img.shields.io/badge/Java-11%2B-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![C++](https://img.shields.io/badge/C%2B%2B-17-00599C?style=for-the-badge&logo=cplusplus&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-wrapper-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![libGDX](https://img.shields.io/badge/libGDX-1.13.0-e74a45?style=for-the-badge)
![Firebase](https://img.shields.io/badge/Firebase-integrated-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

Nebula's Edge is a desktop space shooter inspired by classic Space Invaders gameplay. It combines a libGDX client, a Java game server, Firebase-backed account and configuration services, and a C++ physics engine exposed to Java through JNI.

## Highlights

- Single-player and multiplayer gameplay loops.
- Desktop rendering and input through libGDX/LWJGL3.
- Java HTTP and UDP server for account flows and real-time game state.
- Firebase integration for authentication, database-backed server settings, and user data.
- Native C++ physics engine packaged as a shared library and Java archive.
- Generated Javadocs for both client and server modules.

## Repository Layout

```text
.
|-- client/              # libGDX desktop client
|-- server/              # Java HTTP/UDP backend
|-- src/main/native/     # JNI-enabled C++ physics engine source
|-- src/main/java/       # Java bridge for the physics engine
|-- physics-engine/      # standalone/native physics sources
|-- docs/                # generated ClientJavadoc and ServerJavadoc
|-- test/                # native physics integration test
`-- Makefile             # builds JNI library and PhysicsEngine.jar
```

## Prerequisites

- JDK 11 or newer.
- `JAVA_HOME` set to your JDK installation.
- Gradle is not required globally; both apps include Gradle wrappers.
- A C++17 compiler such as `g++`.
- `make` and basic Unix-style shell utilities for the root native build.
- Firebase project/configuration access for authentication and database-backed runtime settings.

On Windows, run the native build from an environment that provides `make`, `g++`, `mkdir`, `cp`, and `rm`, such as Git Bash, MSYS2, MinGW, or WSL.

## Quick Start

1. Clone the repository.

   ```bash
   git clone https://github.com/AbhirathA/Nebula-s-Edge.git
   cd Nebula-s-Edge
   ```

2. Build the native physics engine from the repository root.

   ```bash
   make all
   ```

   This creates `PhysicsEngine.jar` and the platform shared library under `server/src/main/java/org/spaceinvaders/gameEngine/libs/`.

3. Start the server in a new terminal.

   ```bash
   cd server
   ./gradlew run
   ```

   On Windows PowerShell:

   ```powershell
   cd server
   .\gradlew.bat run
   ```

4. Start the desktop client in another terminal.

   ```bash
   cd client
   ./gradlew lwjgl3:run
   ```

   On Windows PowerShell:

   ```powershell
   cd client
   .\gradlew.bat lwjgl3:run
   ```

## Common Commands

| Command | Location | Description |
| --- | --- | --- |
| `make all` | repository root | Builds the JNI physics library and `PhysicsEngine.jar`. |
| `make test` | repository root | Builds and runs the native physics integration test. |
| `./gradlew run` | `server/` | Starts the Java backend. |
| `./gradlew test` | `server/` | Runs server tests. |
| `./gradlew lwjgl3:run` | `client/` | Starts the desktop game client. |
| `./gradlew lwjgl3:jar` | `client/` | Builds a runnable desktop client jar. |
| `./gradlew build` | `client/` or `server/` | Builds the selected Gradle project. |

## Runtime Notes

- The server HTTP API listens on port `8080`.
- Multiplayer UDP traffic uses port `9090`.
- Single-player UDP sessions start at port `9091`.
- The client loads server IP and port information from Firebase Realtime Database.
- If the client cannot connect, confirm Firebase `serverInfo` values match the machine running the server.

## Documentation

- Client Javadocs: `docs/ClientJavadoc/index.html`
- Server Javadocs: `docs/ServerJavadoc/index.html`
- Client module guide: `client/README.md`
- Server module guide: `server/README.md`

## Tech Stack

- Java 11
- C++17
- Gradle wrapper
- libGDX 1.13.0
- LWJGL3
- Firebase Admin SDK
- Gson
- Logback
- JNI
