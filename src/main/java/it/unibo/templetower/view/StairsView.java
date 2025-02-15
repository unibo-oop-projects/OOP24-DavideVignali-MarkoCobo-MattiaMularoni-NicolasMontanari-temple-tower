package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.beans.binding.Bindings;

/**
 * {@inheritDoc}.
 */
public class StairsView {
    private static final int SPACING = 20;
    private static final int OTHER5 = 50;
    private static final int OTHER4 = 40;
    private static final double VELOCITY = 3.0;

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

        final Label message = new Label("Do you want to go to the next floor?");
        message.styleProperty().bind(Bindings.concat("-fx-font-size: ", root.widthProperty().divide(OTHER4).asString(),
                "px; -fx-text-fill: black;"));

        final Button btYes = new Button("Yes");
        final Button btNo = new Button("No");

        btYes.styleProperty().bind(Bindings.concat("-fx-font-size: ", root.widthProperty().divide(OTHER5).asString(),
                "px; -fx-padding: ", root.widthProperty().divide(OTHER5).asString(), "px;"));
        btNo.styleProperty().bind(Bindings.concat("-fx-font-size: ", root.widthProperty().divide(OTHER5).asString(),
                "px; -fx-padding: ", root.widthProperty().divide(OTHER5).asString(), "px;"));

        layout.getChildren().addAll(message, btYes, btNo);
        root.getChildren().add(layout);

        final String videoPath = StairsView.class.getResource("/video/stairs.mp4").toExternalForm();
        final Media media = new Media(videoPath);
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setRate(VELOCITY);
        final MediaView mediaView = new MediaView(mediaPlayer);

        btYes.setOnAction(_ -> {
            controller.goToNextFloor();
            root.getChildren().clear();
            root.getChildren().add(mediaView);

            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());
            mediaView.setPreserveRatio(false);

            mediaPlayer.play(); // Avvia il video

            // Quando il video finisce, cambia scena
            mediaPlayer.setOnEndOfMedia(() -> {
                if (!controller.isBossTime()) {
                    manager.switchTo("main_floor_view");
                } else {
                    manager.switchTo("combat_view");
                }
            });
        });

        btNo.setOnAction(event -> {
            manager.switchTo("main_floor_view"); // Torna alla scena precedente
        });

        return root;
    }
}
