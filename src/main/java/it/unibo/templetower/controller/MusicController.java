package it.unibo.templetower.controller;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Controller class for managing background music in the game.
 * Implements the Singleton pattern to ensure only one instance controls the audio.
 */
public final class MusicController {

    private static final float DEFAULT_VOLUME = -10.0f;
    private static MusicController instance;
    private Clip audioClip;

    /**
     * Returns the singleton instance of MusicController.
     * @return the unique instance of MusicController
     */
    public static MusicController getInstance() {
        if (instance == null) {
            instance = new MusicController();
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
            InputStream audioStream = getClass().getClassLoader()
                    .getResourceAsStream("audio/" + musicFile);

            if (audioStream == null) {
                System.out.println("File audio non trovato: " + musicFile);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(audioStream)
            );

            audioClip = AudioSystem.getClip();
            audioClip.open(audioInput);

            // Imposta il volume
            FloatControl gainControl
                    = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(DEFAULT_VOLUME);

            // Avvia la nuova musica
            audioClip.setFramePosition(0);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento della musica: " + e.getMessage());
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
