# Nebula's Edge Client

The libGDX desktop game for Nebula's Edge: screens, HUD, rendering, audio, and the networking that talks to the game server. See the [root README](../README.md) for the big picture and screenshots.

## Run

Start the server first (`cd ../server && ./gradlew run`), then:

```bash
./gradlew lwjgl3:run                        # server on this machine
./gradlew lwjgl3:run -Pserver=192.168.1.20  # server elsewhere on the network
```

On Windows use `.\gradlew.bat`. Instead of `-Pserver`, you can set the `NEBULA_SERVER` environment variable. `-PhttpPort` / `NEBULA_HTTP_PORT` override the HTTP port (default `8080`). Press **F12** in game to save a screenshot to `screenshots/`.

## Modules

| Module | Purpose |
| --- | --- |
| `core` | Screens, HUD, gameplay rendering, asset loading, audio, and networking |
| `lwjgl3` | Desktop launcher (1440×810 window) and packaging config |
| `assets` | Hand-drawn textures, fonts, music, sound effects |

## Code map (`core/src/main/java/com/spaceinvaders`)

| Package | What's in it |
| --- | --- |
| `backend` | `UDPClient`: binds a free UDP port, joins a world via the HTTP handshake, streams input every frame, and receives snapshots on a background thread |
| `backend.auth` | `AuthenticationManager` (signup, login, profile, handshake over HTTP), `ServerInfo` (where the server is) |
| `frontend.screens` | Login gateway, login, signup, main menu, options, pause, gameplay, game over, victory |
| `frontend.gameplay` | Renderers for ships, enemies, asteroids, bullets, black holes, and power-ups |
| `frontend.ui` / `background` | Health bar, timer, overlays, starfield, planets |
| `frontend.managers` | Screen stack, assets, music, sounds |

The client renders in pixel-art units: the camera shows 240×135 pixels of a 1200×675 world, and menus are laid out on a 480×270 stage. The server works in units 10× finer.

## Useful tasks

| Task | Description |
| --- | --- |
| `lwjgl3:run` | Run the game |
| `lwjgl3:jar` | Runnable fat jar in `lwjgl3/build/libs/` |
| `core:javadoc` | API docs (a copy is published in `../docs/ClientJavadoc`) |
| `build` | Compile and package everything |
