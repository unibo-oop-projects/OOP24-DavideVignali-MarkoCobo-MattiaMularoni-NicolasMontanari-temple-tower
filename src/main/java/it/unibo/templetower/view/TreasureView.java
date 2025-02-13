package it.unibo.templetower.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.templetower.controller.GameController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

/**
 * View class responsible for displaying the treasure room scene.
 * This class manages the treasure discovery sequence including
 * video playback and weapon selection dialog.
 */
public final class TreasureView {
    private static final Logger LOGGER = LoggerFactory.getLogger(TreasureView.class);
    private static final int BUTTON_FONT_SIZE = 20;
    private static final double WEAPON_DAMAGE = 0.8;
    private static final int DAMAGE_BAR_WIDTH = 200;
    private static final int BUTTON_SPACING = 20; // Added constant for HBox spacing
    private static final int IMAGE_SIZE = 100;
    private static final int PADDING = 10;

    /**
     * Creates and returns the treasure room scene.
     * 
     * @param manager    the scene manager to handle scene transitions
     * @param controller the game controller to handle game logic
     * @return          the created Scene object
     */
    public StackPane createScene(final SceneManager manager, final GameController controller) {
        // Creazione del layout radice (StackPane)
        final StackPane root = new StackPane();

        // Imposta l'immagine di sfondo sullo StackPane (dietro ai bottoni)
        final String bgImageUrl = getClass().getResource("/images/combat_room.jpg").toExternalForm();
        root.setStyle("-fx-background-image: url('" + bgImageUrl + "'); -fx-background-size: cover;");

        // Creazione dei bottoni "Apri" e "Esci"
        final Button openButton = new Button("Apri");
        final Button exitButton = new Button("Esci");
        openButton.getStyleClass().add("openExitButton");
        exitButton.getStyleClass().add("openExitButton");

        // Rendere i bottoni più grandi impostando dimensioni e padding
        openButton.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + "px; -fx-padding: 15px 30px;");
        exitButton.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + "px; -fx-padding: 15px 30px;");

        // Contenitore orizzontale per i bottoni, centrato
        final HBox buttonContainer = new HBox(BUTTON_SPACING); // Use constant instead of magic number
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(openButton, exitButton);

        // Aggiunta iniziale del container dei bottoni al layout radice
        root.getChildren().add(buttonContainer);

        // Preparazione del video
        final String videoPath = getClass().getResource("/video/treasure.mp4").toExternalForm();
        final Media media = new Media(videoPath);
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        final MediaView mediaView = new MediaView(mediaPlayer);

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

        // Azione del bottone "Esci": esce dalla stanza (in questo esempio termina
        // l'applicazione)
        exitButton.setOnAction(e -> {
            LOGGER.info("Player chose to exit the room");
            manager.switchTo("main_floor_view");
        });

        // Al termine della riproduzione del video, mostra il popup
        mediaPlayer.setOnEndOfMedia(() -> Platform.runLater(() -> {
            // Mostra il popup e aspetta la sua chiusura
            showWeaponPopup(() -> {
                // Dopo la chiusura del popup, torna alla main floor view
                manager.switchTo("main_floor_view");
            });
        }));

        root.getStylesheets().add(getClass().getResource("/css/Treasure.css").toExternalForm());

        return root;
    }

    /**
     * Shows a popup dialog for weapon selection.
     * 
     * @param onClose callback to be executed when the dialog is closed
     */
    private void showWeaponPopup(final Runnable onClose) {
        final Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Oggetto Trovato!");
        dialog.setHeaderText("Hai trovato un'arma!");

        // Aggiungiamo un ButtonType per permettere la chiusura con la X
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        final Image image = new Image(getClass().getResource("/images/Gun-PNG-File.png").toExternalForm());
        final ImageView imageView = new ImageView(image);
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);

        final ProgressBar damageBar = new ProgressBar(WEAPON_DAMAGE);
        damageBar.setPrefWidth(DAMAGE_BAR_WIDTH);
        final Label damageLabel = new Label("Danno: 80%");

        final HBox weaponInfo = new HBox(PADDING, imageView, damageLabel);
        final VBox content = new VBox(PADDING, weaponInfo, damageBar);

        final Button takeButton = new Button("Take");
        final Button leaveButton = new Button("Leave");

        takeButton.setOnAction(event -> {
            LOGGER.info("Player took the weapon");
            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        leaveButton.setOnAction(event -> {
            LOGGER.info("Player left the weapon");
            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        final HBox buttonBox = new HBox(PADDING, takeButton, leaveButton);
        final VBox layout = new VBox(PADDING, content, buttonBox);
        layout.setPadding(new Insets(PADDING));

        dialog.getDialogPane().setContent(layout);

        // 🔹 Se l'utente chiude il popup con la X, esegue comunque onClose()
        dialog.setOnCloseRequest(event -> {
            if (onClose != null) {
                onClose.run();
            }
        });

        dialog.showAndWait();
    }

}
