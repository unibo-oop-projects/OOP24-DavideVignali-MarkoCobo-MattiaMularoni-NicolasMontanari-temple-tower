package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.util.Optional;

public class TreasureView {

    public Scene createScene(SceneManager manager, GameController controller) {
        // Creazione del layout radice (StackPane)
        StackPane root = new StackPane();

        // Imposta l'immagine di sfondo sullo StackPane (dietro ai bottoni)
        String bgImageUrl = getClass().getResource("/images/combat_room.jpg").toExternalForm();
        root.setStyle("-fx-background-image: url('" + bgImageUrl + "'); -fx-background-size: cover;");

        // Creazione dei bottoni "Apri" e "Esci"
        Button openButton = new Button("Apri");
        Button exitButton = new Button("Esci");
        openButton.getStyleClass().add("openExitButton");
        exitButton.getStyleClass().add("openExitButton");
        
        // Rendere i bottoni più grandi impostando dimensioni e padding
        openButton.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
        exitButton.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
        
        // Contenitore orizzontale per i bottoni, centrato
        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(openButton, exitButton);

        // Aggiunta iniziale del container dei bottoni al layout radice
        root.getChildren().add(buttonContainer);

        // Preparazione del video
        String videoPath = getClass().getResource("/video/treasure.mp4").toExternalForm();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Associa le dimensioni del video alla scena
        mediaView.fitWidthProperty().bind(root.widthProperty());
        mediaView.fitHeightProperty().bind(root.heightProperty());
        mediaView.setPreserveRatio(false); // Rimuove i bordi forzando l'adattamento alla finestra

        // Allinea il video al centro per evitare margini indesiderati
        StackPane.setAlignment(mediaView, Pos.CENTER);

        // Azione del bottone "Apri": rimuove i bottoni, aggiunge il video e lo avvia
        openButton.setOnAction(e -> {
            root.getChildren().remove(buttonContainer);
            root.getChildren().add(mediaView);
            mediaPlayer.play();
        });


        // Azione del bottone "Esci": esce dalla stanza (in questo esempio termina l'applicazione)
        exitButton.setOnAction(e -> {
            System.out.println("Hai scelto di uscire dalla stanza!");
            // Qui puoi richiamare un metodo del SceneManager per passare a un'altra scena
            manager.switchTo("main_floor_view");
            Platform.exit();
        });

        // Al termine della riproduzione del video, mostra il popup
        mediaPlayer.setOnEndOfMedia(() -> Platform.runLater(this::showWeaponPopup));

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/Treasure.css").toExternalForm());

        return scene;
    }

    private void showWeaponPopup() {
        // Creazione del Dialog per l'arma
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Oggetto Trovato!");
        dialog.setHeaderText("Hai trovato un'arma!");

        // Definizione dei bottoni "Take" e "Leave"
        ButtonType takeButton = new ButtonType("Take", ButtonBar.ButtonData.OK_DONE);
        ButtonType leaveButton = new ButtonType("Leave", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(takeButton, leaveButton);

        // Caricamento dell'immagine dell'arma
        Image image = new Image(getClass().getResource("/images/Gun-PNG-File.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        // Creazione della progress bar per il danno dell'arma (80% di danno)
        ProgressBar damageBar = new ProgressBar(0.8);
        damageBar.setPrefWidth(200);
        Label damageLabel = new Label("Danno: 80%");

        // Layout del contenuto del dialog
        VBox content = new VBox(10);
        HBox weaponInfo = new HBox(10, imageView, damageLabel);
        content.getChildren().addAll(weaponInfo, damageBar);
        dialog.getDialogPane().setContent(content);

        // Mostra il dialogo e attende la scelta dell'utente
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == takeButton) {
            System.out.println("Hai preso l'arma!");
        } else {
            System.out.println("Hai lasciato l'arma!");
        }
    }
}
