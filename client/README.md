# Nebula's Edge Client

![Java](https://img.shields.io/badge/Java-11%2B-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![libGDX](https://img.shields.io/badge/libGDX-1.13.0-e74a45?style=for-the-badge)
![LWJGL3](https://img.shields.io/badge/Desktop-LWJGL3-2b2d42?style=for-the-badge)
![Gradle](https://img.shields.io/badge/Gradle-wrapper-02303A?style=for-the-badge&logo=gradle&logoColor=white)

The client is the libGDX desktop game application for Nebula's Edge. It contains the frontend screens, gameplay rendering, asset loading, audio, Firebase client integration, and UDP networking used to communicate with the game server.

## Modules

| Module | Purpose |
| --- | --- |
| `core` | Shared game logic, screens, UI, networking, Firebase helpers, and gameplay entities. |
| `lwjgl3` | Desktop launcher and packaging configuration for LWJGL3. |
| `assets` | Textures, fonts, music, sounds, and generated asset index. |

## Prerequisites

- JDK 11 or newer.
- Internet access for first-time dependency resolution.
- A running Nebula's Edge server for multiplayer and backend-backed flows.
- Firebase Realtime Database values for server IP and ports.

## Run The Client

From the `client` directory:

```bash
./gradlew lwjgl3:run
```

On Windows PowerShell:

```powershell
.\gradlew.bat lwjgl3:run
```

The desktop launcher starts from `lwjgl3/src/main/java/com/spaceinvaders/lwjgl3/Lwjgl3Launcher.java`.

## Build

Build all client modules:

```bash
./gradlew build
```

Build a runnable desktop jar:

```bash
./gradlew lwjgl3:jar
```

The jar is written to:

```text
lwjgl3/build/libs/space-invaders-1.0.0.jar
```

## Useful Gradle Tasks

| Task | Description |
| --- | --- |
| `lwjgl3:run` | Runs the desktop client. |
| `lwjgl3:jar` | Creates a runnable desktop jar. |
| `build` | Compiles and packages the project. |
| `test` | Runs available tests. |
| `clean` | Removes build output. |
| `generateAssetList` | Regenerates `assets/assets.txt`. |

## Configuration Notes

- The client fetches server connection details from Firebase Realtime Database.
- UDP communication uses the server values returned by `ServerInfo`.
- Local assets are loaded from the `assets` directory during desktop runs.
- Java compatibility is configured for Java 11 in Gradle.

## Related Docs

- Root project setup: `../README.md`
- Generated client Javadocs: `../docs/ClientJavadoc/index.html`
