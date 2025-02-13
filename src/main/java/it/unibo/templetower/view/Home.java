package it.unibo.templetower.view;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Home screen view class that manages the initial game screen and background
 * music.
 * This class is not designed for extension.
 */
public final class Home {
    private static final float MUSIC_VOLUME = -10.0f;
    private static final Logger LOGGER = LoggerFactory.getLogger(Home.class);

    private Clip audioClip;

    /**
     * Creates and returns the home scene with background image and menu button.
     *
     * @param manager The scene manager to handle scene transitions
     * @return Scene object representing the home screen
     * @throws FileNotFoundException if background image resource cannot be found
     */
    public StackPane createScene(final SceneManager manager) throws FileNotFoundException {
        // Create root container
        final StackPane root = new StackPane();

        // Set up background image
        final InputStream backgroundStream = getClass().getClassLoader()
                .getResourceAsStream("images/Schermatainiziale.png");
        if (backgroundStream == null) {
            throw new FileNotFoundException("Could not find background image: images/Schermatainiziale.png");
        }

        // Create and configure background
        final ImageView background = new ImageView(new Image(backgroundStream));
        background.setPreserveRatio(false);

        // Make background responsive to window resizing
        root.widthProperty().addListener((obs, old, newVal) -> background.setFitWidth(newVal.doubleValue()));
        root.heightProperty().addListener((obs, old, newVal) -> background.setFitHeight(newVal.doubleValue()));

        // Create content layout
        final VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);

        // Add difficulty menu button
        final Button difficultyButton = new Button("Go to Enter Menu");
        difficultyButton.setOnAction(e -> {
            // Non fermiamo più la musica quando cambiamo scena
            manager.switchTo("enter_menu");
        });
        content.getChildren().add(difficultyButton);

        // Combine background and content
        root.getChildren().addAll(background, content);

        try {
            final InputStream audioStream = getClass().getClassLoader()
                    .getResourceAsStream("sounds/musicadisottofondo.wav");
            if (audioStream == null) {
                LOGGER.error("Audio file not found!");
            }
            final AudioInputStream audioInput = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(audioStream));
            audioClip = AudioSystem.getClip();
            audioClip.open(audioInput);
            // Set volume
            final FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(MUSIC_VOLUME);
            // Start music immediately
            audioClip.setFramePosition(0);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (javax.sound.sampled.LineUnavailableException e) {
            LOGGER.error("Audio line unavailable: {}", e.getMessage());
        } catch (javax.sound.sampled.UnsupportedAudioFileException e) {
            LOGGER.error("Unsupported audio format: {}", e.getMessage());
        } catch (java.io.IOException e) {
            LOGGER.error("IO error while loading music: {}", e.getMessage());
        }
        return root;
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
