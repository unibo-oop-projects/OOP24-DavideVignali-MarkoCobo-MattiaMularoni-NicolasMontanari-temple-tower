package it.unibo.templetower.view;

import java.io.InputStream;

import it.unibo.templetower.controller.MusicController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DifficultyMenu {

    public Scene createScene(SceneManager manager) {
        // Create root container
        StackPane root = new StackPane();

        // Set up background image
        InputStream backgroundStream = getClass().getClassLoader()
                .getResourceAsStream("images/menu.png");
        ImageView background = new ImageView(new Image(backgroundStream));

        // Configure background properties
        background.setPreserveRatio(false);
        background.setFitWidth(800);
        background.setFitHeight(600);

        // Make background responsive to window resizing
        root.widthProperty().addListener((obs, old, newVal)
                -> background.setFitWidth(newVal.doubleValue()));
        root.heightProperty().addListener((obs, old, newVal)
                -> background.setFitHeight(newVal.doubleValue()));

        // Create difficulty buttons layout
        VBox buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER);

        // Create difficulty buttons
        Button easyButton = new Button("FACILE");
        Button mediumButton = new Button("INTERMEDIO");
        Button hardButton = new Button("DIFFICILE");

        // Set button actions
       easyButton.setOnAction(e -> {
            MusicController.getInstance().startNewMusic("sounds/musicacombattimento1.wav");
            manager.switchTo("main_floor_view");
        });
        
        mediumButton.setOnAction(e -> {
            MusicController.getInstance().startNewMusic("sounds/musicacombattimento1.wav");
            manager.switchTo("main_floor_view");
        });
        
        hardButton.setOnAction(e -> {
            MusicController.getInstance().startNewMusic("sounds/musicacombattimento1.wav");
            manager.switchTo("main_floor_view");
        });

        // Add buttons to container
        buttonContainer.getChildren().addAll(
                easyButton,
                mediumButton,
                hardButton
        );

        // Combine background and buttons
        root.getChildren().addAll(background, buttonContainer);

        // Create and return the scene
        return new Scene(root, 800, 600);
    }
}
