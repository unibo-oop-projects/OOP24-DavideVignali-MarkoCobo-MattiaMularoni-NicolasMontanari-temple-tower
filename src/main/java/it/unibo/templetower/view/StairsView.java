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

public class StairsView {
    private Label message;
    private Button btYes, btNo;
    private String videoPath;
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private VBox layout;

    public Scene createScene(SceneManager manager, GameController controller) {
        // Creazione della label con il messaggio
        message = new Label("Do you want to go to the next floor?");
        message.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");

        // Creazione dei pulsanti
        btYes = new Button("Yes");
        btNo = new Button("No");

        // Rendere i bottoni più grandi impostando dimensioni e padding
        btYes.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
        btNo.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");

        // Preparazione del video
        videoPath = getClass().getResource("/video/treasure.mp4").toExternalForm();
        media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);

        // Layout iniziale con messaggio e pulsanti
        layout = new VBox(20, message, btYes, btNo);
        layout.setAlignment(Pos.CENTER);

        // Contenitore principale per gestire il passaggio alla modalità video
        StackPane root = new StackPane(layout);

        btYes.setOnAction(event -> {
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

        return new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        });
            manager.switchTo("main_floor_view"); // Torna alla scena precedente
        btNo.setOnAction(event -> {
    }
}
