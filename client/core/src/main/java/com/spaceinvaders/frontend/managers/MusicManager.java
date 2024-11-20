package com.spaceinvaders.frontend.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

public class MusicManager implements Disposable {
    private HashMap<String, Music> musicTracks = new HashMap<>();
    private Music currentMusic;
    private float volume = 0.5f;

    public void loadMusic(String key, String filePath) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        music.setLooping(true);
        musicTracks.put(key, music);
    }

    public void play(String key) {
        if(currentMusic != null) {
            currentMusic.stop();
        }
        currentMusic = musicTracks.get(key);
        if(currentMusic != null) {
            currentMusic.setVolume(volume);
            currentMusic.play();
        } else {
            System.out.println("Music track with key '" + key + "' not found!");
        }
    }

    public void pause() {
        if(currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.pause();
        }
    }

    public void resume() {
        if(currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    public void stop() {
        if(currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if(currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }

    @Override
    public void dispose() {

    }
}
