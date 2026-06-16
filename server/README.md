# Nebula's Edge Server

![Java](https://img.shields.io/badge/Java-11%2B-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-wrapper-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-Admin%20SDK-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![JNI](https://img.shields.io/badge/JNI-native%20physics-4B5563?style=for-the-badge)

The server is the Java backend for Nebula's Edge. It exposes HTTP handlers for account/data flows, coordinates UDP game sessions, and loads the JNI physics engine used by the multiplayer game loop.

## Responsibilities

- Start the HTTP API on port `8080`.
- Handle signup, handshake, and data retrieval endpoints.
- Coordinate real-time UDP game state.
- Load the native physics engine from `src/main/java/org/spaceinvaders/gameEngine/libs`.
- Connect to Firebase through the Firebase Admin SDK.

## Prerequisites

- JDK 11 or newer.
- Native physics artifacts built from the repository root with `make all`.
- Firebase credentials/configuration required by the project.
- UDP/HTTP ports available on the development machine.

## Build Native Physics First

From the repository root:

```bash
make all
```

This generates the files expected by the server:

```text
server/src/main/java/org/spaceinvaders/gameEngine/libs/PhysicsEngine.jar
server/src/main/java/org/spaceinvaders/gameEngine/libs/PhysicsEngine.dll
```

On macOS or Linux, the shared library extension will be `.dylib` or `.so`.

## Run The Server

From the `server` directory:

```bash
./gradlew run
```

On Windows PowerShell:

```powershell
.\gradlew.bat run
```

The Gradle `run` task automatically sets:

```text
-Djava.library.path=src/main/java/org/spaceinvaders/gameEngine/libs
```

## Network Defaults

| Service | Port | Source |
| --- | ---: | --- |
| HTTP API | `8080` | `ServerInfo.HTTP_PORT` |
| Multiplayer UDP | `9090` | `ServerInfo.UDP_MULTI_PLAYER_PORT` |
| Single-player UDP | `9091` | `ServerInfo.UDP_SINGLE_PLAYER_PORT` |

## HTTP Endpoints

| Endpoint | Handler |
| --- | --- |
| `/signup` | `SignUpHandler` |
| `/getData` | `GetDataHandler` |
| `/handshake` | `HandshakeHandler` |

## Useful Gradle Tasks

| Task | Description |
| --- | --- |
| `run` | Starts the server. |
| `build` | Compiles and packages the server. |
| `test` | Runs server tests. |
| `clean` | Removes build output. |

## Related Docs

- Root project setup: `../README.md`
- Generated server Javadocs: `../docs/ServerJavadoc/index.html`
