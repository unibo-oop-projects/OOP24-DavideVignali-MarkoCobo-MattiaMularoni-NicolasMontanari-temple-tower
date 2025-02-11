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
    public Scene createScene(SceneManager manager, GameController controller) {
        // Creazione della label con il messaggio
        Label message = new Label("Do you want to go to the next floor?");
        
        // Creazione dei pulsanti
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        // Preparazione del video
        String videoPath = getClass().getResource("/video/treasure.mp4").toExternalForm();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Layout iniziale con messaggio e pulsanti
        VBox layout = new VBox(20, message, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        
        // Contenitore principale per gestire il passaggio alla modalità video
        StackPane root = new StackPane(layout);
        
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
        
        return new Scene(root, 800, 600);
    }
}
