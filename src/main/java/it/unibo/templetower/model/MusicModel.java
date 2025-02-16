package it.unibo.templetower.model;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings; // added import

/**
 * Class responsible for managing music playback and volume control.
 */
public final class MusicModel {

    private static final float VOLUME_MULTIPLIER = 20.0f;
    private Clip audioClip;
    private boolean isPlaying;
    private float volume;

    /**
     * Constructs a new MusicModel with default settings.
     * Initial state is not playing with full volume.
     */
    public MusicModel() {
        this.isPlaying = false;
        this.volume = 1.0f;
    }

    /**
     * Checks if audio is currently playing.
     * @return true if audio is playing, false otherwise
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Sets the playing state of the audio.
     * @param playing the new playing state
     */
    public void setIsPlaying(final boolean playing) {
        this.isPlaying = playing;
    }

    /**
     * Gets the current audio clip.
     * @return the current audio clip's position in microseconds, or -1 if no clip is set
     */
    public long getAudioPosition() {
        return audioClip != null ? audioClip.getMicrosecondPosition() : -1;
    }

    /**
     * Sets the audio clip to be played.
     * @param clip the audio clip to set
     */
    @SuppressFBWarnings(value = "EI2", justification = "Defensive copy is not feasible for Clip; external mutation is acceptable")
    public void setAudioClip(final Clip clip) {
        if (this.audioClip != null) {
            this.audioClip.close();
        }
        // Store a null value if clip is null
        if (clip == null) {
            this.audioClip = null;
            return;
        }
        // We can't create a deep copy of Clip, but we can ensure proper state management
        this.audioClip = clip;
        // Ensure volume is properly set on the new clip
        if (this.audioClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            setVolume(this.volume);
        }
    }

    /**
     * Gets the current volume level.
     * @return the current volume level between 0.0 and 1.0
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Sets the volume level and updates the audio clip if one is loaded.
     * @param volume the new volume level between 0.0 and 1.0
     */
    public void setVolume(final float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        if (audioClip != null) {
            final FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(VOLUME_MULTIPLIER * (float) Math.log10(volume));
        }
    }
}
