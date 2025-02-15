package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.io.InputStream;

import it.unibo.templetower.controller.MusicController;
import it.unibo.templetower.utils.BackgroundUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Home screen view class that manages the initial game screen and background
 * music. This class is not designed for extension.
 */
public final class StartupView {

    /**
     * Creates and returns the home scene with background image and menu button.
     *
     * @param manager The scene manager to handle scene transitions
     * @return Scene object representing the home screen
     * @throws FileNotFoundException if background image resource cannot be
     * found
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

        // Make background responsive using BackgroundUtils
        BackgroundUtils.setupBackgroundResizing(root, background);

        // Create content layout
        final VBox content = new VBox(10);
        content.setAlignment(Pos.BOTTOM_CENTER);

        // Add difficulty menu button
        final Button difficultyButton = new Button("Go to Enter Menu");
        difficultyButton.setOnAction(e -> manager.switchTo("enter_menu"));

        // Add both buttons to the content VBox
        content.getChildren().addAll(difficultyButton);

        // Combine background and content
        root.getChildren().addAll(background, content);

        MusicController.getInstance().startMusic("sounds/musicadisottofondo.wav");

        return root;
    }
}
