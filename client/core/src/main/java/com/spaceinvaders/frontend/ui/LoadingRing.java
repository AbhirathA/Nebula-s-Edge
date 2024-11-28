package com.spaceinvaders.frontend.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.spaceinvaders.frontend.managers.MyAssetManager;

import java.util.ArrayList;

public class LoadingRing extends Actor {
    private final Animation<TextureRegion> animation;
    private float stateTime = 0f;

    public LoadingRing(MyAssetManager assetManager, float x, float y) {
        Texture loadingRingTexture = assetManager.get("textures/loadingSpriteSheet.png", Texture.class);
        TextureRegion[][] frames = TextureRegion.split(loadingRingTexture, 10, 10);
        Array<TextureRegion> animationFrames = new Array<>();

        for(int i=0; i<12; i++) {
            animationFrames.add(frames[0][i]);
        }

        this.animation = new Animation<>(0.1f, animationFrames, Animation.PlayMode.LOOP);
        setSize(20, 20);
        setPosition(x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta; // Update the time to progress the animation
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }
}
