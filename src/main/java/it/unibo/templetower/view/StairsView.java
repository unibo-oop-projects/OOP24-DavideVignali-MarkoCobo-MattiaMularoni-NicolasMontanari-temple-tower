package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * Represents the view for the stairs scene.
 */
public final class StairsView {

    private static final int SPACING = 20;
    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;

    /**
     * Creates the scene for the stairs view.
     *
     * @param manager the scene manager
     * @param controller the game controller
     * @return the created scene
     */
    public Scene createScene(final SceneManager manager, final GameController controller) {
        // Creazione della label con il messaggio
        final Label message = new Label("Do you want to go to the next floor?");

        // Creazione dei pulsanti
        final Button yesButton = new Button("Yes");
        final Button noButton = new Button("No");

        // Preparazione del video
        final String videoPath = getClass().getResource("/video/treasure.mp4").toExternalForm();
        final Media media = new Media(videoPath);
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        final MediaView mediaView = new MediaView(mediaPlayer);

        // Layout iniziale con messaggio e pulsanti
        final VBox layout = new VBox(SPACING, message, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);

        // Contenitore principale per gestire il passaggio alla modalità video
        final StackPane root = new StackPane(layout);

        yesButton.setOnAction(event -> {
            controller.goToNextFloor();
            // Rimuove tutto e aggiunge solo il video a tutta la finestra
            root.getChildren().clear();
            root.getChildren().add(mediaView);

            // Adatta il video alla finestra
            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());
            mediaView.setPreserveRatio(false);

            mediaPlayer.play(); // Avvia il video

            // Quando il video finisce, cambia scena
            mediaPlayer.setOnEndOfMedia(() -> manager.switchTo("main_floor_view"));
        });

        noButton.setOnAction(event -> {
            manager.switchTo("main_floor_view"); // Torna alla scena precedente
        });

        return new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    }
}
