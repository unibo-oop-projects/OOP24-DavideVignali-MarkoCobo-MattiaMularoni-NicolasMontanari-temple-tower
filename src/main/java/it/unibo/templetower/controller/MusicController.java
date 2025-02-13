package it.unibo.templetower.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.templetower.model.MusicModel;

/**
 * Controller class for managing background music in the game. Implements the
 * Singleton pattern to ensure only one instance controls the audio.
 */
public final class MusicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicController.class);
    private static final float DEFAULT_VOLUME = -10.0f;
    private static volatile MusicController instance;
    private Clip currentClip;
    private final MusicModel musicModel;

    private MusicController() {
        // Private constructor to prevent instantiation
        this.musicModel = new MusicModel();
    }

    /**
     * Returns the singleton instance of MusicController.
     *
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
     *
     * @param musicFile the name of the music file to play, located in the audio
     * resources folder
     */
    public void startMusic(final String musicFile) {
        LOGGER.info("Attempting to start music: {}", musicFile);

        try {
            // Prova prima con il path completo
            String resourcePath = "sounds/" + musicFile;
            LOGGER.info("Trying to load resource from: {}", resourcePath);

            InputStream audioStream = getClass().getClassLoader()
                    .getResourceAsStream(resourcePath);

            // Se non trova il file, prova senza la cartella sounds
            if (audioStream == null) {
                LOGGER.warn("File not found in sounds directory, trying direct filename: {}", musicFile);
                audioStream = getClass().getClassLoader().getResourceAsStream(musicFile);
            }

            if (audioStream == null) {
                LOGGER.error("Audio file not found in any location: {}", musicFile);
                // Stampa tutti i dettagli utili per il debug
                LOGGER.error("Current working directory: {}", System.getProperty("user.dir"));
                return;
            }

            LOGGER.info("Audio file found, creating AudioInputStream");
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(audioStream)
            );

            LOGGER.info("Creating and opening clip");
            currentClip = AudioSystem.getClip();
            currentClip.open(audioInput);

            LOGGER.info("Setting up volume control");
            if (currentClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl
                        = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(DEFAULT_VOLUME);
                LOGGER.info("Volume control set successfully");
            } else {
                LOGGER.warn("Volume control not supported for this clip");
            }

            LOGGER.info("Configuring clip in music model");
            musicModel.setAudioClip(currentClip);

            LOGGER.info("Starting playback");
            currentClip.setFramePosition(0);
            currentClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicModel.setIsPlaying(true);

            LOGGER.info("Music started successfully");

        } catch (LineUnavailableException e) {
            LOGGER.error("Audio line unavailable: {}", e.getMessage(), e);
        } catch (UnsupportedAudioFileException e) {
            LOGGER.error("Unsupported audio format: {}", e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error("IO error while loading audio: {}", e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error while starting music: {}", e.getMessage(), e);
        }
    }

    /**
     * Stops the currently playing music track, if any.
     */
    public void stopMusic() {
        LOGGER.info("Attempting to stop music");
        if (musicModel.isPlaying() && currentClip != null) {
            currentClip.stop();
            musicModel.setIsPlaying(false);
            LOGGER.info("Music stopped successfully");
        } else {
            LOGGER.info("No music playing to stop");
        }
    }

    public void startNewMusic(final String musicFile) {
        if (musicModel.isPlaying() && currentClip != null) {
            currentClip.stop();
            musicModel.setIsPlaying(false);
        }
    }

}
