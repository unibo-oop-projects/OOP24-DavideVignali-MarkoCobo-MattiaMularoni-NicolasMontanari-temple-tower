package it.unibo.templetower.view;

import java.io.InputStream;

import it.unibo.templetower.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The DifficultyMenu class represents the menu where the player can select the game difficulty.
 */
public final class DifficultyMenu {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BUTTON_SPACING = 10;

    /**
     * Creates the scene for the difficulty menu.
     *
     * @param manager the scene manager
     * @param controller the game controller
     * @return the created scene
     */
    public Scene createScene(final SceneManager manager, final GameController controller) {
        // Create root container
        StackPane root = new StackPane();

        // Set up background image
        InputStream backgroundStream = getClass().getClassLoader()
                .getResourceAsStream("images/menu.png");
        ImageView background = new ImageView(new Image(backgroundStream));

        // Configure background properties
        background.setPreserveRatio(false);
        background.setFitWidth(WIDTH);
        background.setFitHeight(HEIGHT);

        // Make background responsive to window resizing
        root.widthProperty().addListener((obs, old, newVal)
                -> background.setFitWidth(newVal.doubleValue()));
        root.heightProperty().addListener((obs, old, newVal)
                -> background.setFitHeight(newVal.doubleValue()));

        // Create difficulty buttons layout
        VBox buttonContainer = new VBox(BUTTON_SPACING);
        buttonContainer.setAlignment(Pos.CENTER);

        // Create difficulty buttons
        Button easyButton = new Button("FACILE");
        Button mediumButton = new Button("INTERMEDIO");
        Button hardButton = new Button("DIFFICILE");

        // Set button actions
        easyButton.setOnAction(e -> manager.switchTo("main_floor_view"));
        mediumButton.setOnAction(e -> manager.switchTo("main_floor_view"));
        hardButton.setOnAction(e -> manager.switchTo("main_floor_view"));

        // Add buttons to container
        buttonContainer.getChildren().addAll(
                easyButton,
                mediumButton,
                hardButton
        );

        // Combine background and buttons
        root.getChildren().addAll(background, buttonContainer);

        // Create and return the scene
        return new Scene(root, WIDTH, HEIGHT);
    }
}
