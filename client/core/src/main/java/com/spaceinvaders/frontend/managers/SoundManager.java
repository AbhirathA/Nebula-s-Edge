package com.spaceinvaders.frontend.managers;

import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class SoundManager {
    private HashMap<String, Sound> sounds = new HashMap<>();
    private float volume = 0.5f;

    public void loadSounds (MyAssetManager assetManager) {
        sounds.put("buttonClick", assetManager.get("sounds/mixkit-winning-a-coin-video-game-2069.wav", Sound.class));
    }

    public void play (String key) {
        sounds.get(key).play(volume);
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
