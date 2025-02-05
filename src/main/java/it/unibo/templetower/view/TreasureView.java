package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.util.Optional;

public class TreasureView {

    public Scene createScene(SceneManager manager, GameController controller) {
        String videoPath = getClass().getResource("/video/treasure.mp4").toExternalForm();

        // Creazione del Media e MediaPlayer
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        // Creazione del MediaView
        MediaView mediaView = new MediaView(mediaPlayer);

        // StackPane per centrare il video
        StackPane root = new StackPane(mediaView);

        // Lega le dimensioni del video alle dimensioni dello StackPane
        mediaView.fitWidthProperty().bind(root.widthProperty());
        mediaView.fitHeightProperty().bind(root.heightProperty());
        mediaView.setPreserveRatio(true);

        // Assicurarsi che il video copra tutta la finestra
        mediaView.setStyle("-fx-background-color: black; -fx-video-transform: scale(1.1);");

        // Timeline per mostrare il popup dopo 9 secondi
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(9), event -> {
            Platform.runLater(this::showWeaponPopup); // Esegue il popup in un momento sicuro
        }));
        timeline.setCycleCount(1);
        timeline.play();

        return new Scene(root, 800, 600);
    }

    private void showWeaponPopup() {
        // Creazione del Dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Oggetto Trovato!");
        dialog.setHeaderText("Hai trovato un'arma!");

        // Aggiunta bottoni "Take" e "Leave"
        ButtonType takeButton = new ButtonType("Take", ButtonBar.ButtonData.OK_DONE);
        ButtonType leaveButton = new ButtonType("Leave", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(takeButton, leaveButton);

        // Caricamento dell'immagine dell'arma
        Image image = new Image(getClass().getResource("/images/Gun-PNG-File.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        // Creazione della progress bar per il danno dell'arma
        ProgressBar damageBar = new ProgressBar(0.8); // 80% di danno
        damageBar.setPrefWidth(200);
        Label damageLabel = new Label("Danno: 80%");

        // Layout del contenuto
        VBox content = new VBox(10);
        HBox weaponInfo = new HBox(10, imageView, damageLabel);
        content.getChildren().addAll(weaponInfo, damageBar);
        dialog.getDialogPane().setContent(content);

        // Mostra il dialogo e aspetta la scelta dell'utente
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == takeButton) {
            System.out.println("Hai preso l'arma!");
        } else {
            System.out.println("Hai lasciato l'arma!");
        }
    }
}
