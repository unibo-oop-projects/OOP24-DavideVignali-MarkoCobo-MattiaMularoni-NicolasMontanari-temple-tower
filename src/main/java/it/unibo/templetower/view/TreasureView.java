package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class TreasureView {
    private Label message;
    private String imageUrl;
    private Button btOpen, btExt;
    private HBox btContainer;
    private VBox layout;
    private String videoPath;
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    public Scene createScene(SceneManager manager, GameController controller) {
        // Creazione del layout radice (StackPane)
        StackPane root = new StackPane();

        message = new Label("Do you want to open the chest?");
        message.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");

        // Imposta l'immagine di sfondo sullo StackPane (dietro ai bottoni)
        imageUrl = getClass().getResource("/images/combat_room.jpg").toExternalForm();
        root.setStyle("-fx-background-image: url('" + imageUrl + "'); -fx-background-size: cover;");

        // Creazione dei bottoni "Apri" e "Esci"
        btOpen = new Button("Apri");
        btExt = new Button("Esci");

        btOpen.getStyleClass().add("openExitButton");
        btExt.getStyleClass().add("openExitButton");

        // Rendere i bottoni più grandi impostando dimensioni e padding
        btOpen.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
        btExt.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");

        // Contenitore orizzontale per i bottoni, centrato
        btContainer = new HBox(20);
        btContainer.setAlignment(Pos.CENTER);
        btContainer.getChildren().addAll(btOpen, btExt);

        layout = new VBox(20, message, btContainer);
        layout.setAlignment(Pos.CENTER);

        // Aggiunta iniziale del container dei bottoni al layout radice
        root.getChildren().add(layout);

        // Preparazione del video
        videoPath = getClass().getResource("/video/treasure.mp4").toExternalForm();
        media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);

        // Associa le dimensioni del video alla scena
        mediaView.fitWidthProperty().bind(root.widthProperty());
        mediaView.fitHeightProperty().bind(root.heightProperty());
        mediaView.setPreserveRatio(false); // Rimuove i bordi forzando l'adattamento alla finestra

        // Allinea il video al centro per evitare margini indesiderati
        StackPane.setAlignment(mediaView, Pos.CENTER);

        // Azione del bottone "Apri": rimuove i bottoni, aggiunge il video e lo avvia
        btOpen.setOnAction(e -> {
            root.getChildren().remove(btContainer);
            root.getChildren().add(mediaView);
            mediaPlayer.play();
        });

        // Azione del bottone "Esci": esce dalla stanza (in questo esempio termina
        // l'applicazione)
        btExt.setOnAction(e -> {
            System.out.println("Hai scelto di uscire dalla stanza!");
            // Qui puoi richiamare un metodo del SceneManager per passare a un'altra scena
            manager.switchTo("main_floor_view");
            // Platform.exit();
        });

        // Al termine della riproduzione del video, mostra il popup
        mediaPlayer.setOnEndOfMedia(() -> Platform.runLater(() -> {
            showWeaponPopup(() -> {// Mostra il popup e aspetta la sua chiusura
                manager.switchTo("main_floor_view");// Dopo la chiusura del popup, torna alla main floor view
            });
        }));

        Scene scene = new Scene(root, 800, 600);

        scene.getStylesheets().add(getClass().getResource("/css/Treasure.css").toExternalForm());

        return scene;
    }

    private void showWeaponPopup(Runnable onClose) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Oggetto Trovato!");
        dialog.setHeaderText("Hai trovato un'arma!");

        // Aggiungiamo un ButtonType per permettere la chiusura con la X
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Image image = new Image(getClass().getResource("/images/Gun-PNG-File.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        ProgressBar damageBar = new ProgressBar(0.8);
        damageBar.setPrefWidth(200);
        Label damageLabel = new Label("Danno: 80%");

        HBox weaponInfo = new HBox(10, imageView, damageLabel);
        VBox content = new VBox(10, weaponInfo, damageBar);

        Button btTake = new Button("Take");
        Button btLeave = new Button("Leave");

        btTake.setOnAction(event -> {
            System.out.println("Hai preso l'arma!");
            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        btLeave.setOnAction(event -> {
            System.out.println("Hai lasciato l'arma!");
            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        HBox buttonBox = new HBox(10, btTake, btLeave);
        VBox layout = new VBox(10, content, buttonBox);
        layout.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(layout);

        // Se l'utente chiude il popup con la X, esegue comunque onClose()
        dialog.setOnCloseRequest(event -> {
            if (onClose != null) {
                onClose.run();
            }
        });

        dialog.showAndWait();
    }

}
