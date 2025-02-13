package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.io.InputStream;

import it.unibo.templetower.controller.MusicController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Home screen view class that manages the initial game screen and background
 * music. This class is not designed for extension.
 */
public final class Home {

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;


    /**
     * Creates and returns the home scene with background image and menu button.
     *
     * @param manager The scene manager to handle scene transitions
     * @return Scene object representing the home screen
     * @throws FileNotFoundException if background image resource cannot be
     * found
     */
    public Scene createScene(final SceneManager manager) throws FileNotFoundException {
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
        background.setFitWidth(WINDOW_WIDTH);
        background.setFitHeight(WINDOW_HEIGHT);

        // Make background responsive to window resizing
        root.widthProperty().addListener((obs, old, newVal)
                -> background.setFitWidth(newVal.doubleValue()));
        root.heightProperty().addListener((obs, old, newVal)
                -> background.setFitHeight(newVal.doubleValue()));

        // Create content layout
        final VBox content = new VBox(10);
        content.setAlignment(Pos.BOTTOM_CENTER);

        // Add difficulty menu button
        final Button difficultyButton = new Button("Go to Enter Menu");
        difficultyButton.setOnAction(e -> {
            // Non fermiamo più la musica quando cambiamo scena
            manager.switchTo("enter_menu");
        });
        // Mute button with image
        Image image = new Image("images/muto.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        final Button muteButton = new Button();  // Create button without text
        muteButton.setGraphic(imageView);
        muteButton.setPrefSize(30, 30);  // Set fixed size for square button
        muteButton.setOnAction(e -> {
            MusicController.getInstance().stopMusic();
            
        });

           // Create container for mute button positioned at bottom right
        final VBox muteContainer = new VBox(muteButton);
        muteContainer.setAlignment(Pos.BOTTOM_RIGHT);

        // Add both buttons to the content VBox
        content.getChildren().addAll(difficultyButton, muteContainer);

        // Combine background and content
        root.getChildren().addAll(background, content);

        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        MusicController.getInstance().startMusic("sounds/musicadisottofondo.wav");

        return scene;
    }

    

}
