package it.unibo.templetower.view;

import java.io.InputStream;

import it.unibo.templetower.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The DifficultyMenu class represents the menu where the player can select the
 * game difficulty.
 */
public final class DifficultyMenu {
        private static final int BUTTON_SPACING = 10;

        /**
         * Creates the scene for the difficulty menu.
         *
         * @param manager    the scene manager
         * @param controller the game controller
         * @return the created scene
         */
        public StackPane createScene(final StageManager manager, final GameController controller) {
                // Create root container
                final StackPane root = new StackPane();

                // Set up background image
                final InputStream backgroundStream = getClass().getClassLoader()
                                .getResourceAsStream("images/menu.png");
                final ImageView background = new ImageView(new Image(backgroundStream));

                // Configure background properties
                background.setPreserveRatio(false);

                // Make background responsive to window resizing
                root.widthProperty().addListener((obs, old, newVal) -> background.setFitWidth(newVal.doubleValue()));
                root.heightProperty().addListener((obs, old, newVal) -> background.setFitHeight(newVal.doubleValue()));

                // Create difficulty buttons layout
                final VBox buttonContainer = new VBox(BUTTON_SPACING);
                buttonContainer.setAlignment(Pos.CENTER);

                // Create difficulty buttons
                final Button easyButton = new Button("FACILE");
                final Button mediumButton = new Button("INTERMEDIO");
                final Button hardButton = new Button("DIFFICILE");

                // Set button actions
                easyButton.setOnAction(e -> manager.switchTo("main_floor_view"));
                mediumButton.setOnAction(e -> manager.switchTo("main_floor_view"));
                hardButton.setOnAction(e -> manager.switchTo("main_floor_view"));

                // Add buttons to container
                buttonContainer.getChildren().addAll(
                                easyButton,
                                mediumButton,
                                hardButton);

                // Combine background and buttons
                root.getChildren().addAll(background, buttonContainer);

                // Create and return the pane
                return root;
        }
}
