package it.unibo.templetower.controller;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class for managing background music in the game.
 * Implements the Singleton pattern to ensure only one instance controls the audio.
 */
public final class MusicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicController.class);
    private static final float DEFAULT_VOLUME = -10.0f;
    private static volatile MusicController instance;
    private Clip audioClip;

    private MusicController() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the singleton instance of MusicController.
     * @return the unique instance of MusicController
     */
    public static MusicController getInstance() {
        if (instance == null) {
            synchronized (MusicController.class) {
                if (instance == null) {
                    instance = new MusicController();
                }
            }
        }
        return instance;
    }

    /**
     * Starts playing a new music track, stopping any currently playing music.
     * @param musicFile the name of the music file to play, located in the audio resources folder
     */
    public void startNewMusic(final String musicFile) {
        try {
            // Ferma la musica precedente se sta suonando
            stopMusic();

            // Carica la nuova musica
            final InputStream audioStream = getClass().getClassLoader()
                    .getResourceAsStream("audio/" + musicFile);

            if (audioStream == null) {
                LOGGER.error("File audio non trovato: {}", musicFile);
                return;
            }

            final AudioInputStream audioInput = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(audioStream)
            );

            audioClip = AudioSystem.getClip();
            audioClip.open(audioInput);

            // Imposta il volume
            final FloatControl gainControl
                    = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(DEFAULT_VOLUME);

            // Avvia la nuova musica
            audioClip.setFramePosition(0);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (javax.sound.sampled.UnsupportedAudioFileException e) {
            LOGGER.error("Audio file format not supported: {}", e.getMessage(), e);
        } catch (javax.sound.sampled.LineUnavailableException e) {
            LOGGER.error("Audio line unavailable: {}", e.getMessage(), e);
        } catch (java.io.IOException e) {
            LOGGER.error("IO error while loading audio: {}", e.getMessage(), e);
        }
    }

    /**
     * Stops the currently playing music track, if any.
     */
    public void stopMusic() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
    }

}
