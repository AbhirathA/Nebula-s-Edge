// Instantiates the WebAssembly physics engine (physics.js / physics.wasm, built from
// src/main/native with Emscripten) and exposes it to the game as window.NebulaPhysics.
// index.html waits for this promise before starting the game.
window.nebulaPhysicsReady = createNebulaPhysics().then(function (module) {
    window.NebulaPhysics = module;
}).catch(function (error) {
    console.error("Could not load the physics engine", error);
});
