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

public class Home {

    public Scene createScene(SceneManager manager) throws FileNotFoundException {
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
        background.setFitWidth(400);
        background.setFitHeight(300);

        // Make background responsive to window resizing
        root.widthProperty().addListener((obs, old, newVal)
                -> background.setFitWidth(newVal.doubleValue()));
        root.heightProperty().addListener((obs, old, newVal)
                -> background.setFitHeight(newVal.doubleValue()));

        // Create content layout
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);

        // Add difficulty menu button
        Button difficultyButton = new Button("Go to Difficulty Menu");
        difficultyButton.setOnAction(e -> manager.switchTo("difficulty_menu"));
        content.getChildren().add(difficultyButton);

        // Combine background and content
        root.getChildren().addAll(background, content);

        Scene scene = new Scene(root, 400, 300);

        // Create and return the scene
        return scene;
    }

}
