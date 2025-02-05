package it.unibo.templetower.controller;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import it.unibo.templetower.model.MusicModel;


public class MusicController {

    private  MusicModel model ;

    public MusicController(MusicModel model) {
        this.model = model;
    }

    public void loadMusic(String filePath) {
         try {
            File musicFile = new File(filePath);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            model.setAudioClip(clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (model.getAudioClip() != null && !model.isPlaying()) {
            model.getAudioClip().setFramePosition(0);
            model.getAudioClip().loop(Clip.LOOP_CONTINUOUSLY);
            model.setIsPlaying(true);
        }

    }

    public void stop() {
        if (model.getAudioClip() != null && model.isPlaying()) {
            model.getAudioClip().stop();
            model.setIsPlaying(false);
        }
    }

    public void setVolume(float volume) {
        model.setVolume(volume);
    }

}
