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
            final String resourcePath = "sounds/" + musicFile;
            LOGGER.info("Trying to load resource from: {}", resourcePath);

            InputStream audioStream = getClass().getClassLoader()
                    .getResourceAsStream(resourcePath);

            // Se non trova il file, prova senza la cartella sounds
            if (audioStream == null) {
                audioStream = getClass().getClassLoader().getResourceAsStream(musicFile);
            }

            if (audioStream == null) {
                LOGGER.error("Audio file not found in any location: {}", musicFile);
                // Stampa tutti i dettagli utili per il debug
                LOGGER.error("Current working directory: {}", System.getProperty("user.dir"));
                return;
            }

            LOGGER.info("Audio file found, creating AudioInputStream");
            final AudioInputStream audioInput = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(audioStream)
            );

            LOGGER.info("Creating and opening clip");
            currentClip = AudioSystem.getClip();
            currentClip.open(audioInput);

            LOGGER.info("Setting up volume control");
            if (currentClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                final FloatControl gainControl
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
        }
    }

    /**
     * Restarts the currently playing music track, if any.
     */
    public void restartMusic() {
        if (musicModel.isPlaying() && currentClip == null) {
            musicModel.setIsPlaying(true);
            LOGGER.info("Music restarted successfully");
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

    /**
     * Starts a new music track, stopping any currently playing music.
     *
     * @param musicFile the name of the music file to play, located in the audio
     * resources folder
     */
    public void startNewMusic(final String musicFile) {
        if (musicModel.isPlaying() && currentClip != null) {
            currentClip.stop();
            musicModel.setIsPlaying(false);
        }
    }

    /**
     * Turn up the volume current music track.
     */
    public void raiseVol() {
        if (currentClip != null && musicModel.isPlaying()) {

            final FloatControl gainControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            final float currentVolume = gainControl.getValue();
            final float maxVol = gainControl.getMaximum();
            final float raise = 1.0f;

            if (currentVolume + raise > maxVol) {
                gainControl.setValue(maxVol);
            } else {
                gainControl.setValue(currentVolume + raise);
            }
            LOGGER.info("Volume raised to: {}", gainControl.getValue());

        }

    }

    /**
     * Turn down the volume current music track.
     */
    public void lowerVol() {
        if (currentClip != null && musicModel.isPlaying()) {

            final FloatControl gainControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            final float currentVolume = gainControl.getValue();
            final float minVol = gainControl.getMinimum();
            final float lower = -1.0f;

            if (currentVolume + lower < minVol) {
                gainControl.setValue(minVol);
            } else {
                gainControl.setValue(currentVolume + lower);
            }
            LOGGER.info("Volume lowered to: {}", gainControl.getValue());

        }
    }

}
