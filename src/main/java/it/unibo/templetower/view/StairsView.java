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
 * {@inheritDoc}.
 */
public class StairsView {
    private static final int SPACING = 20;
    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;
    private static final double VELOCITY = 3.0;

    /**
     * 
     * Creates the scene for the stairs view.
     *
     * @param manager    the scene manager
     * @param controller the game controller
     * @return the created scene
     */
    public Scene createScene(final SceneManager manager, final GameController controller) {
        // Creazione della label con il messaggio
        final Label message = new Label("Do you want to go to the next floor?");
        message.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");

        // Creazione dei pulsanti
        final Button btYes = new Button("Yes");
        final Button btNo = new Button("No");

        // Rendere i bottoni più grandi impostando dimensioni e padding
        btYes.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
        btNo.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");

        // Preparazione del video
        final String videoPath = StairsView.class.getResource("/video/treasure.mp4").toExternalForm();
        final Media media = new Media(videoPath);
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setRate(VELOCITY);
        final MediaView mediaView = new MediaView(mediaPlayer);

        // Layout iniziale con messaggio e pulsanti
        final VBox layout = new VBox(SPACING, message, btYes, btNo);
        layout.setAlignment(Pos.CENTER);

        // Contenitore principale per gestire il passaggio alla modalità video
        final StackPane root = new StackPane(layout);

        btYes.setOnAction(_ -> {

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

        btNo.setOnAction(_ -> {
            manager.switchTo("main_floor_view"); // Torna alla scena precedente
        });

        return new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    }
}
