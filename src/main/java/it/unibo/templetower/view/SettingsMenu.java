package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.io.InputStream;

import it.unibo.templetower.controller.MusicController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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

        Image image1 = new Image("images/alzavol.png");
        ImageView imageView1 = new ImageView(image1);
        imageView1.setFitWidth(30);
        imageView1.setFitHeight(30);

        Image image2 = new Image("images/abbassavol.png");
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitWidth(30);
        imageView2.setFitHeight(30);

        final Button raiseButton = new Button();  // Create button without text
        raiseButton.setGraphic(imageView1);
        raiseButton.setPrefSize(30, 30);  // Set fixed size for square button
        raiseButton.setOnAction(e -> {
            
        });

        final Button lowerButton = new Button();  // Create button without text
        lowerButton.setGraphic(imageView2);
        lowerButton.setPrefSize(30, 30);  // Set fixed size for square button
        lowerButton.setOnAction(e -> {
           
        });
        final HBox volumeContainer = new HBox(10);
        volumeContainer.getChildren().addAll(raiseButton, lowerButton);
        volumeContainer.setAlignment(Pos.CENTER);
        
        // Combine background and content
        root.getChildren().addAll(background, content, muteContainer, volumeContainer);

        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        return scene;
    }
}
