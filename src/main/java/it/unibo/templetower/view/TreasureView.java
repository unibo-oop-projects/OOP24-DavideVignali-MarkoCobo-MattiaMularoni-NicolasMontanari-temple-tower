package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * View class responsible for displaying the treasure room scene.
 * This class manages the treasure discovery sequence including
 * video playback and weapon selection dialog.
 */
public final class TreasureView {
    private HBox btContainer;

    private static final Logger LOGGER = LoggerFactory.getLogger(TreasureView.class);
    private static final int BUTTON_FONT_SIZE = 20;
    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;
    private static final int DAMAGE_BAR_WIDTH = 200;
    private static final int IMAGE_SIZE = 100;
    private static final int PADDING = 10;
    private static final int DIALOG_WIDTH = 250;
    private static final int DIALOG_HEIGHT = 80;
    private static final int VBOX = 50;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final String MAIN_VIEW = "main_floor_view";

    /**
     * Creates and returns the treasure room scene.
     * 
     * @param manager    the scene manager to handle scene transitions
     * @param controller the game controller to handle game logic
     * @return the created Scene object
     */
    public Scene createScene(final SceneManager manager, final GameController controller) {
        // Creazione del layout radice (StackPane)
        final StackPane root = new StackPane();

        final Label message = new Label("Do you want to open the chest?");
        message.setStyle("-fx-font-size: 24px; -fx-text-fill: black;");

        // Imposta l'immagine di sfondo sullo StackPane (dietro ai bottoni)
        final String imageUrl = getClass().getResource("/images/combat_room.jpg").toExternalForm();
        root.setStyle("-fx-background-image: url('" + imageUrl + "'); -fx-background-size: cover;");

        // Creazione dei bottoni "Apri" e "Esci"
        final Button btOpen = new Button("Apri");
        final Button btExt = new Button("Esci");

        btOpen.getStyleClass().add("openExitButton");
        btExt.getStyleClass().add("openExitButton");

        // Rendere i bottoni più grandi impostando dimensioni e padding
        btOpen.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
        btExt.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");

        // Contenitore orizzontale per i bottoni, centrato
        btContainer = new HBox(BUTTON_FONT_SIZE);
        btContainer.setAlignment(Pos.CENTER);
        btContainer.getChildren().addAll(btOpen, btExt);

        final VBox layout = new VBox(BUTTON_FONT_SIZE, message, btContainer);
        layout.setAlignment(Pos.CENTER);

        // Aggiunta iniziale del container dei bottoni al layout radice
        root.getChildren().add(layout);

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
        btOpen.setOnAction(e -> {
            root.getChildren().remove(btContainer);
            root.getChildren().add(mediaView);
            mediaPlayer.play();
        });

        // Azione del bottone "Esci": esce dalla stanza (in questo esempio termina
        // l'applicazione)
        LOGGER.info("Player chose to exit the room");
        btExt.setOnAction(e -> {
            manager.switchTo(MAIN_VIEW);
        });

        // Al termine della riproduzione del video, mostra il popup
        mediaPlayer.setOnEndOfMedia(() -> Platform.runLater(() -> {
            if (controller.getElementTreasure() == 1) {
                showWeaponPopup(controller, manager, () -> { // Mostra il popup e aspetta la sua chiusura
                    manager.switchTo(MAIN_VIEW); // Dopo la chiusura del popup, torna alla main floor view
                });
            } else if (controller.getElementTreasure() == 2) {
                showXpPopup(manager, () -> { // Mostra il popup e aspetta la sua chiusura
                    manager.switchTo(MAIN_VIEW); // Dopo la chiusura del popup, torna alla main floor view
                });
            }

        }));

        final Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/css/Treasure.css").toExternalForm());

        return scene;
    }

    /**
     * Shows a popup dialog for weapon selection.
     * 
     * @param onClose callback to be executed when the dialog is closed
     * @param controller
     * @param manager
     */
    private void showWeaponPopup(final GameController controller, final SceneManager manager, final Runnable onClose) {
        final Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Oggetto Trovato!");
        dialog.setHeaderText("Hai trovato un'arma!");

        // Aggiungiamo un ButtonType per permettere la chiusura con la X
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        final Image image = new Image(getClass().getResource("/images/Gun-PNG-File.png").toExternalForm());
        final ImageView imageView = new ImageView(image);
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);

        final ProgressBar damageBar = new ProgressBar(controller.getTreasureWeapon().attack().getY());
        damageBar.setPrefWidth(DAMAGE_BAR_WIDTH);
        final Label damageLabel = new Label("Danno: " + controller.getTreasureWeapon().attack().getY());

        final HBox weaponInfo = new HBox(PADDING, imageView, damageLabel);
        final VBox content = new VBox(PADDING, weaponInfo, damageBar);

        final Button btTake = new Button("Take");
        final Button btLeave = new Button("Leave");

        btTake.setOnAction(event -> {
            if (controller.getPlayerWeapons().size() < 3) {
                controller.addPlayerWeapon(controller.getTreasureWeapon(), PADDING);
            } else {
                manager.switchTo("change_weapon_view");
            }

            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        btLeave.setOnAction(event -> {
            LOGGER.info("Player left the weapon");
            manager.switchTo(MAIN_VIEW);
            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        final HBox buttonBox = new HBox(10, btTake, btLeave);
        final VBox layout = new VBox(10, content, buttonBox);
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

    private void showXpPopup(final SceneManager manager, final Runnable onClose) {
        final Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("YOU TAKE XP");
        dialog.setHeaderText(null); // Rimuove il titolo predefinito

        // Imposta la dimensione della finestra
        dialog.getDialogPane().setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Testo grande per il messaggio di sconfitta
        final Label loseLabel = new Label("YOU WON XP");
        loseLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: black;");

        // Aggiungiamo un ButtonType fittizio per abilitare la chiusura con la X
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Bottone "Leave"
        final Button btLeave = new Button("Leave");
        btLeave.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
        btLeave.setPrefSize(DIALOG_WIDTH, DIALOG_HEIGHT); // Aumenta le dimensioni del bottone

        // Azione del bottone per chiudere la finestra
        btLeave.setOnAction(event -> {
            LOGGER.info("WON XP");
            dialog.setResult(null); // Imposta un risultato per chiudere la dialog
            manager.switchTo(MAIN_VIEW);
            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        // Contenitore per il bottone
        final HBox btContainer = new HBox(btLeave);
        btContainer.setAlignment(Pos.CENTER);

        // Contenitore principale con testo e bottone
        final VBox layout = new VBox(VBOX, loseLabel, btContainer);
        layout.setAlignment(Pos.CENTER);

        // Imposta il contenuto della finestra
        dialog.getDialogPane().setContent(layout);

        // Permette la chiusura con la X
        dialog.setOnCloseRequest(event -> {
            LOGGER.info("Popup closed with X");
            dialog.setResult(null);
            if (onClose != null) {
                onClose.run();
            }
        });

        // Mostra la finestra di dialogo
        dialog.showAndWait();
    }

}
