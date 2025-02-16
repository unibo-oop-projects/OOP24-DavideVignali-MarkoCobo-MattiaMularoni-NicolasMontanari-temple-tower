package it.unibo.templetower.view;

import java.io.File;

import it.unibo.templetower.controller.GameController;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * The stairs view for pass to next floor.
 */
public class StairsView {
    private static final int SPACING = 20;
    private static final int OTHER5 = 50;
    private static final int OTHER4 = 40;

    /**
     * Creates the scene for the stairs view.
     *
     * @param manager    the scene manager
     * @param controller the game controller
     * @return the created scene
     */
    public StackPane createScene(final SceneManager manager, final GameController controller) {
        final StackPane root = new StackPane();
        final VBox layout = new VBox(SPACING);
        layout.setAlignment(Pos.CENTER);

        final String bgImage;
        if (!controller.isBossTime()) {
            bgImage = controller.getBackgroundImage();
        } else {
            bgImage = "/Images/final_arena.png";
        }

        final Image backgroundImage;
        try {
            if (!controller.isBossTime()) {
                final File file = new File(bgImage);
                backgroundImage = new Image(file.toURI().toString());
            } else {
                backgroundImage = new Image(StairsView.class.getResource(bgImage).toExternalForm());
            }

            final ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setPreserveRatio(false);
            backgroundView.fitWidthProperty().bind(root.widthProperty());
            backgroundView.fitHeightProperty().bind(root.heightProperty());
            root.getChildren().add(backgroundView);
        } catch (IllegalArgumentException e) {
            final Label errorLabel = new Label("Background image not found.");
            errorLabel.getStyleClass().add("label");
            root.getChildren().add(errorLabel);
        }

        final Label message = new Label("Do you want to go to the next floor?");
        message.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", root.widthProperty().divide(OTHER4).asString(),
                "px; -fx-text-fill: black; -fx-font-weight: bold;"));

        final Button btYes = new Button("Yes");
        final Button btNo = new Button("No");

        btYes.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", root.widthProperty().divide(OTHER5).asString(),
                "px; -fx-padding: ", root.widthProperty().divide(OTHER5).asString(),
                "px; -fx-background-color: black; -fx-text-fill: white;"));

        btNo.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", root.widthProperty().divide(OTHER5).asString(),
                "px; -fx-padding: ", root.widthProperty().divide(OTHER5).asString(),
                "px; -fx-background-color: black; -fx-text-fill: white;"));

        layout.getChildren().addAll(message, btYes, btNo);
        root.getChildren().add(layout);

        btYes.setOnAction(event -> {
            controller.goToNextFloor();
            root.getChildren().clear();

            final Media media = new Media(StairsView.class.getResource("/video/stairs.mp4").toExternalForm());
            final MediaPlayer newMediaPlayer = new MediaPlayer(media);
            final MediaView newMediaView = new MediaView(newMediaPlayer);

            newMediaView.fitWidthProperty().bind(root.widthProperty());
            newMediaView.fitHeightProperty().bind(root.heightProperty());
            newMediaView.setPreserveRatio(false);

            root.getChildren().add(newMediaView);

            newMediaPlayer.setOnReady(() -> {
                newMediaPlayer.play();
            });

            newMediaPlayer.setOnEndOfMedia(() -> {
                if (!controller.isBossTime()) {
                    manager.switchTo("main_floor_view");
                } else {
                    manager.switchTo("combat_view");
                }
                newMediaPlayer.dispose();
            });

            if (newMediaPlayer.getStatus() == MediaPlayer.Status.READY) {
                newMediaPlayer.play();
            }

        });

        btNo.setOnAction(event -> {
            manager.switchTo("main_floor_view");
        });

        return root;
    }
}
