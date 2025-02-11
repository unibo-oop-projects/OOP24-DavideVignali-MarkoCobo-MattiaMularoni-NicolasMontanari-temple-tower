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

/**
 * Class responsible for creating and managing the enter menu scene.
 * This class provides the initial menu interface for the game.
 */
public final class EnterMenu {

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;
    private static final int RIGHT_PADDING = 20;
    private static final int BUTTON_SPACING = 10;

    /**
     * Creates and returns the enter menu scene.
     *
     * @param manager The scene manager to handle scene transitions
     * @return A new Scene object containing the enter menu interface
     * @throws FileNotFoundException if required image resources are not found
     */
    public Scene createScene(final SceneManager manager) throws FileNotFoundException {
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
        background.setFitWidth(WINDOW_WIDTH);
        background.setFitHeight(WINDOW_HEIGHT);

        // Make background responsive to window resizing
        root.widthProperty().addListener((obs, old, newVal)
                -> background.setFitWidth(newVal.doubleValue()));
        root.heightProperty().addListener((obs, old, newVal)
                -> background.setFitHeight(newVal.doubleValue()));

        // Create content layout
        VBox content = new VBox(BUTTON_SPACING);
        content.setAlignment(Pos.CENTER);

        // Add difficulty menu button
        Button difficultyButton = new Button("Go to Difficulty Menu");
        difficultyButton.setOnAction(e -> {
            // Non fermiamo più la musica quando cambiamo scena
            manager.switchTo("difficulty_menu");
        });
        content.getChildren().add(difficultyButton);

        VBox rightButtons = new VBox(BUTTON_SPACING);
        rightButtons.setAlignment(Pos.CENTER_RIGHT);
        rightButtons.setPadding(new Insets(0, RIGHT_PADDING, 0, 0)); // Add right padding

        Button personalizationButton = new Button("Personalization");
        Button leaderBoardButton = new Button("LeaderBoard");
        Button moddingButton = new Button("Modding Menu");
        Button settingsButton = new Button("Settings");

        // Add action handlers for the new buttons
        personalizationButton.setOnAction(e -> {
            // Add your action here
        });
        leaderBoardButton.setOnAction(e -> {
            // Add your action here
        });
        moddingButton.setOnAction(e -> {
            // Add your action here
        });
        settingsButton.setOnAction(e -> {
            // Add your action here
        });

        // Add buttons to rightButtons in the correct order
        rightButtons.getChildren().addAll(personalizationButton, leaderBoardButton, moddingButton, settingsButton);

        // Create main layout using BorderPane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(content);
        mainLayout.setRight(rightButtons);

        // Combine background and content
        root.getChildren().addAll(background, mainLayout);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        return scene;

    }

}
