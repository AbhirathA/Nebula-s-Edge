# Nebula's Edge Server

The Java backend for Nebula's Edge. It serves the account and handshake HTTP API, runs the UDP game worlds, and drives the C++ physics engine over JNI. See the [root README](../README.md) for the big picture and screenshots.

## Run

```bash
./gradlew run          # Windows: .\gradlew.bat run
```

The first run compiles the C++ engine (`src/main/native/*.cpp`) into `build/native/` with the `buildNative` task, so you need a 64-bit `g++` (or `clang++` on macOS) on `PATH`. To use a different compiler, pass `-Pcxx=/path/to/compiler` or set `CXX`. No `make` or external services are required.

## What runs where

| Port | Protocol | Purpose |
| ---: | --- | --- |
| `8080` | HTTP | `/signup`, `/login`, `/getData`, `/handshake` |
| `9090` | UDP | the shared multiplayer world |
| any free port | UDP | one private world per single-player game, created by `/handshake` and shut down when the game ends |

Each world runs two threads:

- **Game thread:** 90 ticks per second. It applies each client's latest input, steps the native physics (`GameEngine` → `com.physics.Manager` → C++), and builds a snapshot for every player. All native calls happen on this thread, because the engine keeps its timers per thread.
- **Network thread:** receives input packets and sends the latest snapshots back. It only accepts packets from `(ip, udpPort)` pairs that completed `/handshake`.

## HTTP API

All endpoints take `POST` with a JSON body.

| Endpoint | Body | Success response |
| --- | --- | --- |
| `/signup` | `{"email": id, "password": pw}` | `User Created` (`402` if the id is taken, `403` if the password is under 6 characters) |
| `/login` | `{"email": id, "password": pw}` | `{"idToken": "<session token>"}` (`401` on bad credentials) |
| `/getData` | `{"idToken": token}` | `{"email", "level", "killCount"}` (`405` if the session is unknown) |
| `/handshake` | `{"type": "SINGLEPLAYER" \| "MULTIPLAYER", "udpPort": n}` | the UDP port of the world to send input to |

UDP input is `{"token": ..., "state": "FORWARD|BACKWARD|LEFT|RIGHT|BULLET..."}`. Snapshots are a JSON `UDPPacket`: your ship id plus lists of ships, enemies, asteroids, bullets, black holes, and power-ups.

## Configuration

| What | Where |
| --- | --- |
| World size, radii, masses, speeds | `src/main/resources/gameConstants.json` |
| Level layout (asteroids, black holes, meteors, power-ups) | `GameEngine.instantiateGameEngineObjects()` |
| Account file | `data/accounts.json` (override with `-Dnebula.accounts=path`). Passwords are salted PBKDF2-SHA256 hashes. |
| Logs | console and `logs/serverLog.log` |

The game originally loaded its constants and accounts from Firebase. That project has since been retired, so the server is now fully self-contained.

## Useful tasks

| Task | Description |
| --- | --- |
| `run` | Build the native library and start the server |
| `buildNative` | Only compile the C++ engine |
| `build` | Compile and package |
| `javadoc` | API docs in `build/docs/javadoc` (a copy is published in `../docs/ServerJavadoc`) |
