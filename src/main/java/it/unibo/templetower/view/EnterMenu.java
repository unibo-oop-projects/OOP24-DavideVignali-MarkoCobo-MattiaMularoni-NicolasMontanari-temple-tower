package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.io.InputStream;

import it.unibo.templetower.utils.BackgroundUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Class responsible for creating and managing the enter menu scene. This class
 * provides the initial menu interface for the game.
 */
public final class EnterMenu {
    private static final int RIGHT_PADDING = 20;
    private static final int BUTTON_SPACING = 10;

    /**
     * Creates and returns the enter menu scene.
     *
     * @param manager The scene manager to handle scene transitions
     * @return A new Scene object containing the enter menu interface
     * @throws FileNotFoundException if required image resources are not found
     */
    public StackPane createScene(final SceneManager manager) throws FileNotFoundException {
        // Create root container
        final StackPane root = new StackPane();

        // Set up background image
        final InputStream backgroundStream = getClass().getClassLoader()
                .getResourceAsStream("images/main-background.png");
        if (backgroundStream == null) {
            throw new FileNotFoundException("Could not find background image: images/main-background.png");
        }

        // Create and configure background
        final ImageView background = new ImageView(new Image(backgroundStream));
        background.setPreserveRatio(false);

        // Make background responsive using BackgroundUtils
        BackgroundUtils.setupBackgroundResizing(root, background);

        // Create content layout
        final VBox content = new VBox(BUTTON_SPACING);
        content.setAlignment(Pos.CENTER);

        // Add difficulty menu button
        final Button difficultyButton = new Button("Go to Difficulty Menu");
        difficultyButton.setOnAction(e -> manager.switchTo("difficulty_menu"));
        content.getChildren().add(difficultyButton);

        final VBox rightButtons = new VBox(BUTTON_SPACING);
        rightButtons.setAlignment(Pos.CENTER_RIGHT);
        rightButtons.setPadding(new Insets(0, RIGHT_PADDING, 0, 0));

        final Button moddingButton = new Button("Modding Menu");
        final Button settingsButton = new Button("Settings");

        // Add action handlers for the new buttons
        moddingButton.setOnAction(e -> manager.switchTo("modding_menu"));
        settingsButton.setOnAction(e -> manager.switchTo("settings_menu"));

        // Add buttons to rightButtons in the correct order
        rightButtons.getChildren().addAll(moddingButton, settingsButton);

        // Create main layout using BorderPane
        final BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(content);
        mainLayout.setRight(rightButtons);

        // Combine background and content
        root.getChildren().addAll(background, mainLayout);

        return root;
    }
}
