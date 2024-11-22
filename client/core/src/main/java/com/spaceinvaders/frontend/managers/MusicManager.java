package com.spaceinvaders.frontend.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

public class MusicManager implements Disposable {
    // HashMap to store music tracks with their corresponding string keys
    private HashMap<String, Music> musicTracks = new HashMap<>();

    // Currently playing music track
    private Music currentMusic;

    // Volume level for the music
    private float volume = 0.5f;

    // Loads a music track from the given file path and stores it in the musicTracks HashMap
    public void loadMusic(String key, String filePath) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(filePath));

        // Set the music to loop indefinitely
        music.setLooping(true);

        musicTracks.put(key, music);
    }

    // Plays the music track associated with the provided key
    public void play(String key) {
        if(currentMusic == musicTracks.get(key)) {
            return;
        }

        // Stop the currently playing music if there is one
        if(currentMusic != null) {
            currentMusic.stop();
        }

        currentMusic = musicTracks.get(key);

        // If the music track is found, set its volume and play it
        if(currentMusic != null) {
            currentMusic.setVolume(volume);
            currentMusic.play();
        } else {
            // If the music track is not found, throw a runtime error
            throw new RuntimeException("Music track with key '" + key + "' not found!");
        }
    }

    // Pauses the currently playing music if it is playing
    public void pause() {
        if(currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.pause();
        }
    }

    // Resumes the currently paused music if it is not playing
    public void resume() {
        if(currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    // Stops the currently playing music if it is playing
    public void stop() {
        if(currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
    }

    // Getter method for the current volume level
    public float getVolume() {
        return volume;
    }

    // Setter method to adjust the volume level, and update the current music's volume
    public void setVolume(float volume) {
        this.volume = volume;
        if(currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }

    // Method to release resources used by the music manager (implemented from the Disposable interface)
    @Override
    public void dispose() {
        for (Music music : musicTracks.values()) {
            music.dispose();
        }

        musicTracks.clear();
    }
}
