package it.unibo.templetower.controller;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public class MusicController {

    private static MusicController instance;
    private Clip audioClip;

    public static MusicController getInstance() {
        if (instance == null) {
            instance = new MusicController();
        }
        return instance;
    }

    public void startNewMusic(String musicFile) {
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
            gainControl.setValue(-10.0f);

            // Avvia la nuova musica
            audioClip.setFramePosition(0);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento della musica: " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
    }

}
