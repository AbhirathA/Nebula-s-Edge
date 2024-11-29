package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spaceinvaders.backend.utils.Coordinate;
import com.spaceinvaders.backend.utils.UDPPacket;
import com.spaceinvaders.frontend.SpaceInvadersGame;
import com.spaceinvaders.frontend.background.StarsBackground;

import java.util.ArrayList;

public class GameplayStage extends Stage {
    private final SpaceInvadersGame game;
    private final StarsBackground starsBackground;
    private final Rocket rocket;
    private final Bullets bullets;
    private final Enemies enemies;
    private final Asteroids asteroids;
    private final Powerups powerups;
    private final Spaceships spaceships;
    private final Blackholes blackholes;
    private final Player player;

    private final float WORLD_WIDTH;
    private final float WORLD_HEIGHT;

    private ArrayList<Coordinate> coordinates = CoordinateTest.generateCoordinates(100);

    private UDPPacket udpPacket;

    public GameplayStage(SpaceInvadersGame game, Viewport viewport, float WORLD_WIDTH, float WORLD_HEIGHT, boolean isMulti) {
        super(viewport);
        this.game = game;
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;

        // Initialize stars background
        starsBackground = new StarsBackground(WORLD_WIDTH, WORLD_HEIGHT, 200);

        // Initialize rocket
        rocket = new Rocket(game.assetManager, WORLD_WIDTH / 2 - 21f / 2f, WORLD_HEIGHT / 2 - 21f / 2f, WORLD_WIDTH, WORLD_HEIGHT);
        addActor(rocket);

        bullets = new Bullets(game.assetManager);
        enemies = new Enemies(game.assetManager);
        asteroids = new Asteroids(game.assetManager);
        powerups = new Powerups(game.assetManager);
        spaceships = new Spaceships(game.assetManager);
        blackholes = new Blackholes(game.assetManager);

        if(isMulti) {
            player = spaceships;
        } else {
            player = enemies;
        }
    }

    @Override
    public void draw() {
        // Render rocket sprite
        game.batch.setProjectionMatrix(getViewport().getCamera().combined);
        super.draw();
    }

    @Override
    public void act(float delta) {
        // Render background
        starsBackground.render(game.shapeRenderer, delta);
        if(udpPacket != null) {
            getBatch().begin();
            asteroids.render(getBatch(), udpPacket.asteroids);
            blackholes.render(getBatch(), udpPacket.blackholes);
            bullets.render(getBatch(), udpPacket.bullets);
            player.render(getBatch(), udpPacket.spaceShips, udpPacket.id);
            powerups.render(getBatch(), udpPacket.powerUpH);
            powerups.render(getBatch(), udpPacket.powerUpP);
            powerups.render(getBatch(), udpPacket.powerUpB);
            getBatch().end();
        }
        super.act(delta);
    }

    public void setUdpPacket(UDPPacket udpPacket) { this.udpPacket = udpPacket; }

    public Sprite getRocketSprite() { return rocket.getRocketSprite(); }
}
