package it.unibo.templetower.view;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Home screen view class that manages the initial game screen and background music.
 * This class is not designed for extension.
 */
public final class Home {

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;
    private static final float MUSIC_VOLUME = -10.0f;

    private Clip audioClip;

    /**
     * Creates and returns the home scene with background image and menu button.
     *
     * @param manager The scene manager to handle scene transitions
     * @return Scene object representing the home screen
     * @throws FileNotFoundException if background image resource cannot be found
     */
    public Scene createScene(final SceneManager manager) throws FileNotFoundException {
        // Create root container
        StackPane root = new StackPane();

        // Set up background image
        InputStream backgroundStream = getClass().getClassLoader()
                .getResourceAsStream("images/Schermatainiziale.png");
        if (backgroundStream == null) {
            throw new FileNotFoundException("Could not find background image: images/Schermatainiziale.png");
        }

        // Create and configure background
        ImageView background = new ImageView(new Image(backgroundStream));
        background.setPreserveRatio(false);
        background.setFitWidth(WINDOW_WIDTH);
        background.setFitHeight(WINDOW_HEIGHT);

        // Make background responsive to window resizing
        root.widthProperty().addListener((obs, old, newVal)
                -> background.setFitWidth(newVal.doubleValue()));
        root.heightProperty().addListener((obs, old, newVal)
                -> background.setFitHeight(newVal.doubleValue()));

        // Create content layout
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);

        // Add difficulty menu button
        Button difficultyButton = new Button("Go to Enter Menu");
        difficultyButton.setOnAction(e -> {
            // Non fermiamo più la musica quando cambiamo scena
            manager.switchTo("enter_menu");
        });
        content.getChildren().add(difficultyButton);

        // Combine background and content
        root.getChildren().addAll(background, content);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        try {
            InputStream audioStream = getClass().getClassLoader()
                    .getResourceAsStream("sounds/musicadisottofondo.wav");

            if (audioStream == null) {
                System.out.println("File audio non trovato!");
                return scene;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(audioStream)
            );

            audioClip = AudioSystem.getClip();
            audioClip.open(audioInput);

            // Imposta il volume
            FloatControl gainControl
                    = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(MUSIC_VOLUME);

            // Avvia la musica immediatamente
            audioClip.setFramePosition(0);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento della musica: " + e.getMessage());
        }

        return scene;

    }

    /**
     * Stops the background music if it is currently playing.
     */
    public void stopMusic() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
    }

}
