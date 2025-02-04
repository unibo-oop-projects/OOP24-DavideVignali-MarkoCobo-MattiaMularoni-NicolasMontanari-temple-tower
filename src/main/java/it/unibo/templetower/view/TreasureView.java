package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class TreasureView {

    public Scene createScene(SceneManager manager, GameController controller) {
        String videoPath = "file:///C:/path/to/treasure.mp4"; // Cambia con il percorso corretto

        // Creazione del Media e MediaPlayer
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true); // Avvia il video automaticamente
        
        // Creazione del MediaView
        MediaView mediaView = new MediaView(mediaPlayer);

        // StackPane per centrare il video
        StackPane root = new StackPane(mediaView);

        // Lega le dimensioni del video alle dimensioni dello StackPane
        mediaView.fitWidthProperty().bind(root.widthProperty());
        mediaView.fitHeightProperty().bind(root.heightProperty());
        mediaView.setPreserveRatio(true);

        // Crea la scena con dimensione di default (che potrà essere ridimensionata)
        return new Scene(root, 800, 600);
    }
}
