package it.unibo.templetower.model;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MusicModel {

    private Clip audioClip;
    private boolean isPlaying;
    private float volume;

    public MusicModel() {
        this.isPlaying = false;
        this.volume = 1.0f;

    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    public Clip getAudioClip() {
        return audioClip;
    }

    public void setAudioClip(Clip clip) {
        this.audioClip = clip;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        if (audioClip != null) {
            FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
        }
    }

}
