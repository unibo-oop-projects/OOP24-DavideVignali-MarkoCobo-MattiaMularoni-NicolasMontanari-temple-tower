package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EnterMenu {

    public Scene createScene(SceneManager manager) throws FileNotFoundException {
        // Create root container
        StackPane root = new StackPane();

        // Set up background image
        InputStream backgroundStream = getClass().getClassLoader()
                .getResourceAsStream("images/Entermenu.png");
        if (backgroundStream == null) {
            throw new FileNotFoundException("Could not find background image: images/Entermenu.png");
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
        difficultyButton.setOnAction(e -> {
            // Non fermiamo più la musica quando cambiamo scena
            manager.switchTo("difficulty_menu");
        });
        content.getChildren().add(difficultyButton);

        VBox rightButtons = new VBox(10);
        rightButtons.setAlignment(Pos.CENTER_RIGHT);
        rightButtons.setPadding(new Insets(0, 20, 0, 0)); // Add right padding

        Button Personalizzationbutton = new Button("Personalization");
        Button LeaderBoardbutton = new Button("LeaderBoard");
        Button Moddingbutton = new Button("Modding Menu");
        Button Settingsbutton = new Button("Settings");

// Add action handlers for the new buttons
        Personalizzationbutton.setOnAction(e -> {
            // Add your action here
        });
        LeaderBoardbutton.setOnAction(e -> {
            // Add your action here
        });
        Moddingbutton.setOnAction(e -> {
            // Add your action here
        });
        Settingsbutton.setOnAction(e -> {
            // Add your action here
        });

// Add buttons to rightButtons in the correct order
        rightButtons.getChildren().addAll(Personalizzationbutton, LeaderBoardbutton, Moddingbutton, Settingsbutton);

        // Create main layout using BorderPane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(content);
        mainLayout.setRight(rightButtons);

        // Combine background and content
        root.getChildren().addAll(background, mainLayout);

        Scene scene = new Scene(root, 400, 300);

        return scene;

    }

}
