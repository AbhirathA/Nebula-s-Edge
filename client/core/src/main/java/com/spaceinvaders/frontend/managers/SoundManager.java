package com.spaceinvaders.frontend.managers;

import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

public class SoundManager {
    // HashMap to store sound assets with their corresponding string keys
    private HashMap<String, Sound> sounds = new HashMap<>();

    // Default volume level for playing sounds
    private float volume = 0.5f;

    // Loads sound assets from the asset manager and stores them in the sounds HashMap
    public void loadSounds(MyAssetManager assetManager) {
        sounds.put("buttonClick", assetManager.get("sounds/mixkit-winning-a-coin-video-game-2069.wav", Sound.class));
        sounds.put("shoot", assetManager.get("sounds/mixkit-short-laser-gun-shot-1670.wav", Sound.class));
        sounds.put("gameOver", assetManager.get("sounds/game-over-38511.mp3", Sound.class));
        sounds.put("victory", assetManager.get("sounds/goodresult-82807.mp3", Sound.class));
        sounds.put("bonusHealth", assetManager.get("sounds/mixkit-unlock-new-item-game-notification-254.wav", Sound.class));
        sounds.put("damageTaken", assetManager.get("sounds/mixkit-unlock-new-item-game-notification-254.wav", Sound.class));
    }

    // Plays the sound associated with the provided key at the current volume
    public void play(String key) {
        sounds.get(key).play(volume);
    }

    // Getter method for the current volume level
    public float getVolume() {
        return volume;
    }

    // Setter method for adjusting the volume level
    public void setVolume(float volume) {
        this.volume = volume;
    }
}
