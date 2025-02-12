package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SettingsMenu {

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;

    public Scene createScene(SceneManager manager) throws FileNotFoundException {
        // Create root container
        final StackPane root = new StackPane();

        // Set up background image
        final InputStream backgroundStream = getClass().getClassLoader()
                .getResourceAsStream("images/settings.png");
        if (backgroundStream == null) {
            throw new FileNotFoundException("Could not find background image: images/settings.png");
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
        final Button difficultyButton = new Button("Return to Enter Menu");
        difficultyButton.setOnAction(e -> {
            
            manager.switchTo("enter_menu");
        });
        content.getChildren().add(difficultyButton);
        
        // Combine background and content
        root.getChildren().addAll(background, content);

        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        return scene;
    }
}
