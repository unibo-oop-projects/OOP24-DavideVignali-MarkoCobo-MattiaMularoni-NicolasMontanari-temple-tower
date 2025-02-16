package it.unibo.templetower.view;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.templetower.controller.GameController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.util.Duration;

/**
 * View class responsible for displaying the treasure room scene.
 * This class manages the treasure discovery sequence including
 * video playback and weapon selection dialog.
 */
public final class TreasureView {
    private static final Logger LOGGER = LoggerFactory.getLogger(TreasureView.class);
    private static final int DAMAGE_BAR_WIDTH = 200;
    private static final int BUTTON_SPACING = 20;
    private static final int BUTTON30 = 30;
    private static final int SPACING4 = 40;
    private static final int PADDING = 10;
    private static final int DIALOG_WIDTH = 800;
    private static final int DIALOG_HEIGHT = 600;
    private static final int VBOX = 50;
    private static final double UNO = 1.5;
    private static final int VBOX2 = 25;
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
    public StackPane createScene(final SceneManager manager, final GameController controller) {
        final StackPane root = new StackPane();

        final VBox mainContainer = new VBox(BUTTON_SPACING);
        mainContainer.setAlignment(Pos.CENTER);

        final Label message = new Label("Do you want to open the chest?");
        message.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", root.widthProperty().divide(BUTTON_SPACING).asString(),
                "px; -fx-text-fill: black; -fx-font-weight: bold;"));
        message.setWrapText(true);

        final String imageUrl = getClass().getResource("/images/combat_room.jpg").toExternalForm();
        root.setStyle("-fx-background-image: url('" + imageUrl
                + "'); -fx-background-size: cover; -fx-background-position: center;");

        final Button btOpen = new Button("Apri");
        final Button btExit = new Button("Esci");

        btOpen.getStyleClass().add("openExitButton");
        btExit.getStyleClass().add("openExitButton");

        btOpen.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", root.widthProperty().divide(VBOX2).asString(),
                "px; -fx-padding: ", root.widthProperty().divide(VBOX).asString(), "px;"));

        btExit.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", root.widthProperty().divide(VBOX2).asString(),
                "px; -fx-padding: ", root.widthProperty().divide(VBOX).asString(), "px;"));

        final HBox buttonContainer = new HBox(SPACING4);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(btOpen, btExit);

        mainContainer.getChildren().addAll(message, buttonContainer);
        root.getChildren().add(mainContainer);

        btOpen.setOnAction(_ -> {
            root.getChildren().remove(buttonContainer);

            final String videoPath = getClass().getResource("/video/treasure.mp4").toExternalForm();

            final Media media = new Media(videoPath);
            final MediaPlayer mediaPlayer = new MediaPlayer(media);
            final MediaView mediaView = new MediaView(mediaPlayer);

            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());
            mediaView.setPreserveRatio(false);
            StackPane.setAlignment(mediaView, Pos.CENTER);

            root.getChildren().add(mediaView);

            mediaPlayer.setOnReady(() -> {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            });

            Platform.runLater(() -> {
                if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.play();
                }
            });

            mediaPlayer.setOnEndOfMedia(() -> Platform.runLater(() -> {
                mediaPlayer.dispose();

                if (controller.getElementTreasure() == 1) {
                    showWeaponPopup(controller, manager, () -> manager.switchTo(MAIN_VIEW));
                } else if (controller.getElementTreasure() == 2) {
                    showXpPopup(controller, manager, () -> manager.switchTo(MAIN_VIEW));
                }
            }));

        });

        LOGGER.info("Player chose to exit the room");
        btExit.setOnAction(_ -> manager.switchTo(MAIN_VIEW));

        root.getStylesheets().add(getClass().getResource("/css/Treasure.css").toExternalForm());

        return root;
    }

    /**
     * Shows a popup dialog for weapon selection.
     * 
     * @param onClose    callback to be executed when the dialog is closed
     * @param controller
     * @param manager
     */
    private void showWeaponPopup(final GameController controller, final SceneManager manager, final Runnable onClose) {
        LOGGER.info("WEAPON pop");
        final Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Oggetto Trovato!");
        dialog.setHeaderText("Hai trovato un'arma: " + controller.getTreasureWeapon().name());

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().setMinSize(DIALOG_WIDTH, DIALOG_HEIGHT);

        final String imagePath = controller.getWeaponPath();
        final File file = new File(imagePath);
        final ImageView imageView = new ImageView(new Image(file.toURI().toString()));
        final int newImageSize = 200;
        imageView.setFitWidth(newImageSize);
        imageView.setFitHeight(newImageSize);

        final Label damageLabel = new Label("Danno: " + controller.getTreasureWeapon().attack().getY());
        damageLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: red;");

        final ProgressBar damageBar = new ProgressBar(controller.getTreasureWeapon().attack().getY());
        damageBar.setPrefWidth(DAMAGE_BAR_WIDTH * UNO); 
        damageBar.setPrefHeight(BUTTON30); 

        final VBox content = new VBox(15, imageView, damageLabel, damageBar);
        content.setAlignment(Pos.CENTER);

        final Button btTake = new Button("Take");
        final Button leaveButton = new Button("Leave");

        btTake.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        leaveButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");

        btTake.setOnAction(_ -> {
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

        leaveButton.setOnAction(_ -> {
            LOGGER.info("Player left the weapon");
            manager.switchTo(MAIN_VIEW);
            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        final HBox buttonBox = new HBox(BUTTON_SPACING, btTake, leaveButton);
        buttonBox.setAlignment(Pos.CENTER);

        final VBox layout = new VBox(BUTTON_SPACING, content, buttonBox);
        layout.setPadding(new Insets(BUTTON_SPACING));
        layout.setAlignment(Pos.CENTER);

        dialog.getDialogPane().setContent(layout);
        DialogUtil.showDialog(dialog, " DAMAGE: " + controller.getTreasureWeapon().attack().getY(), onClose);
    }

    private void showXpPopup(final GameController controller, final SceneManager manager, final Runnable onClose) {
        LOGGER.info("XP");
        final Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("YOU TAKE XP");
        dialog.setHeaderText(null);

        controller.increaseLifePlayer(controller.getXpTreasure());

        dialog.getDialogPane().setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        final Label loseLabel = new Label("YOU WON " + controller.getPlayerLife() + " XP");
        loseLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: black;");

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        final Button btLeave = new Button("Leave");
        btLeave.setStyle("-fx-text-fill: white; -fx-padding: 15px 30px;");
        btLeave.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-background-color: black;");
        btLeave.setPrefSize(DIALOG_WIDTH, DIALOG_HEIGHT);

        btLeave.setOnAction(_ -> {
            LOGGER.info("Player left the weapon");
            manager.switchTo(MAIN_VIEW);
            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        final HBox btContainer = new HBox(btLeave);
        btContainer.setAlignment(Pos.CENTER);

        final VBox layout = new VBox(VBOX, loseLabel, btContainer);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(PADDING));

        dialog.getDialogPane().setContent(layout);

        DialogUtil.showDialog(dialog, "YOU WON XP", onClose);
    }

}
