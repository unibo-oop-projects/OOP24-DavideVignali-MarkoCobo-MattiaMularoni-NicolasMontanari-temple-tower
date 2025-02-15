package it.unibo.templetower.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.templetower.controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Represents the combat view of the game where battles take place.
 * This class is responsible for creating and managing the combat scene.
 */
public final class BossView {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int DIALOG_WIDTH = 250;
    private static final int DIALOG_HEIGHT = 80;
    private static final int VBOX = 50;
    private static final int CHARACTER_SIZE = 150;
    private static final int BUTTON_WIDTH = 150;
    private static final int HEALTH_BAR_WIDTH = 200;
    private static final double INITIAL_HEALTH = 1.0;

    private static final Logger LOGGER = LoggerFactory.getLogger(CombatView.class);

    private ProgressBar playerHealthBar;
    private ProgressBar enemyHealthBar;
    private Button attackButton;
    private Button exitButton;
    private Boolean kill = true;

    /**
     * Creates and returns the combat scene with all necessary UI elements.
     * 
     * @param manager    the scene manager to handle scene transitions
     * @param controller the game controller to handle game logic
     * @return the created combat scene
     */
    public StackPane createScene(final SceneManager manager, final GameController controller) {
        final StackPane root = new StackPane();
        root.getStyleClass().add("root");

        final ImageView backgroundView = new ImageView();
        try {
            final Image backgroundImage = new Image(getClass().getResource("/Images/final_arena.png").toExternalForm());
            backgroundView.setImage(backgroundImage);
            backgroundView.setPreserveRatio(false);
            backgroundView.fitWidthProperty().bind(root.widthProperty());
            backgroundView.fitHeightProperty().bind(root.heightProperty());

            root.getChildren().add(backgroundView);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed to load background image: {}", e.getMessage());
            final Label errorLabel = new Label("Background image not found.");
            errorLabel.getStyleClass().add("label");
            root.getChildren().add(errorLabel);
            return root;
        }

        final VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.BOTTOM_CENTER);

        final ImageView playerImage = new ImageView(
                new Image(getClass().getResource("/Images/playerback.png").toExternalForm()));
        final ImageView bossImage = new ImageView(
                new Image(getClass().getResource("/Images/boss.png").toExternalForm()));

        bossImage.setFitWidth(CHARACTER_SIZE);
        bossImage.setFitHeight(CHARACTER_SIZE);

        playerImage.setFitWidth(CHARACTER_SIZE);
        playerImage.setFitHeight(CHARACTER_SIZE);

        VBox spriteContainer = new VBox(100);
        spriteContainer.getChildren().addAll(bossImage, playerImage);
        spriteContainer.setStyle("-fx-alignment: center;");

        attackButton = new Button("Attack!");
        attackButton.getStyleClass().add("button");

        exitButton = new Button("Exit");
        exitButton.getStyleClass().add("button");

        playerHealthBar = new ProgressBar(INITIAL_HEALTH);
        playerHealthBar.getStyleClass().add("health-bar-player");

        enemyHealthBar = new ProgressBar(controller.getEnemyLifePoints() / 100);
        enemyHealthBar.getStyleClass().add("health-bar-enemy");

        root.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            final double scaleFactor = newWidth.doubleValue() / WINDOW_WIDTH;
            playerImage.setFitWidth(CHARACTER_SIZE * scaleFactor);
            playerImage.setFitHeight(CHARACTER_SIZE * scaleFactor);
            bossImage.setFitWidth(CHARACTER_SIZE * scaleFactor);
            bossImage.setFitHeight(CHARACTER_SIZE * scaleFactor);
            attackButton.setPrefWidth(BUTTON_WIDTH * scaleFactor);
            exitButton.setPrefWidth(BUTTON_WIDTH * scaleFactor);
            playerHealthBar.setPrefWidth(HEALTH_BAR_WIDTH * scaleFactor);
            enemyHealthBar.setPrefWidth(HEALTH_BAR_WIDTH * scaleFactor);
        });

        final VBox rootBox = new VBox();
        rootBox.setAlignment(Pos.BOTTOM_CENTER);

        rootBox.getChildren().add(spriteContainer);

        final BorderPane healthBarsPane = new BorderPane();
        healthBarsPane.setPadding(new Insets(10));

        final Label playerHpLabel = new Label("100 HP");
        playerHpLabel.getStyleClass().add("label");
        final VBox playerHealthBox = new VBox(5, playerHpLabel, playerHealthBar);
        playerHealthBox.setAlignment(Pos.BOTTOM_LEFT);
        healthBarsPane.setLeft(playerHealthBox);

        final Label enemyHpLabel = new Label("100 HP");
        enemyHpLabel.getStyleClass().add("label");
        final VBox enemyHealthBox = new VBox(5, enemyHpLabel, enemyHealthBar);
        enemyHealthBox.setAlignment(Pos.BOTTOM_RIGHT);
        healthBarsPane.setRight(enemyHealthBox);

        exitButton.setOnAction(event -> {
            LOGGER.debug("Boss life points: {}", controller.getEnemyLifePoints());
            manager.switchTo("home");
        });

        attackButton.setOnAction(event -> {
            LOGGER.debug("Boss life points: {}", controller.getEnemyLifePoints());

            ImageView attackImage = new ImageView(new Image(getClass().getResourceAsStream("/Images/flame.gif")));
            attackImage.setFitWidth(100);
            attackImage.setFitHeight(100);
            attackImage.setLayoutX(playerImage.getLayoutX() + 40);
            attackImage.setLayoutY(playerImage.getLayoutY() + 200);

            ((Pane) playerImage.getParent()).getChildren().add(attackImage);

            final double distance = bossImage.getLayoutX() - playerImage.getLayoutX() - 700;

            final Timeline timeline = new Timeline();
            final KeyValue kv = new KeyValue(attackImage.translateYProperty(), distance);
            final KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
            timeline.getKeyFrames().add(kf);

            timeline.setOnFinished(e -> {
                ((Pane) attackImage.getParent()).getChildren().remove(attackImage);

                controller.attackEnemy();

                final PauseTransition pause = new PauseTransition(Duration.millis(200));
                pause.setOnFinished(e1 -> {
                    Platform.runLater(() -> {
                        if (controller.getEnemyLifePoints() <= 0) {
                            enemyHpLabel.setText("0HP");
                            attackButton.setDisable(true);
                            controller.resetPlayerLife();
                            enemyHealthBar.setProgress(0 / 100.0);
                            kill = false;
                        } else if (kill) {
                            controller.attackPlayer();

                            final PauseTransition pause2 = new PauseTransition(Duration.millis(100));
                            pause2.setOnFinished(e3 -> {
                                playerHealthBar.setProgress(controller.getPlayerLife() / 100.0);
                                playerHpLabel.setText(controller.getPlayerLife() + "HP");

                                if (controller.getPlayerLife() <= 0) {
                                    attackButton.setDisable(true);
                                    playerHpLabel.setText("0HP");
                                    controller.gameOver();
                                    popUp(() -> manager.switchTo("home"));
                                }
                            });
                            pause2.play();
                        }

                        playerHealthBar.setProgress(controller.getPlayerLife() / 100.0);
                        enemyHealthBar.setProgress(controller.getEnemyLifePoints() / 100.0);

                        playerHpLabel.setText(controller.getPlayerLife() + "HP");
                        enemyHpLabel.setText(controller.getEnemyLifePoints() + "HP");

                        if (controller.getPlayerLife() <= 0 || controller.getEnemyLifePoints() <= 0) {
                            attackButton.setDisable(true);
                            controller.resetPlayerLife();
                        }
                    });
                });
                pause.play();
            });

            timeline.play();
        });

        final HBox buttonBox = new HBox(20, attackButton, exitButton);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        healthBarsPane.setBottom(buttonBox);

        final VBox topBox = new VBox();
        topBox.setAlignment(Pos.TOP_RIGHT);

        final Button changeWeapon = new Button("CHANGE WEAPON");
        changeWeapon.getStyleClass().add("button");
        final HBox highBox = new HBox(changeWeapon);
        highBox.setAlignment(Pos.TOP_RIGHT);
        topBox.getChildren().add(highBox);

        changeWeapon.setOnAction(event -> {
            manager.switchTo("select_weapon_view");
        });

        rootBox.getChildren().addAll(healthBarsPane, topBox);
        root.getChildren().add(rootBox);

        root.getStylesheets().add(getClass().getResource("/css/Combat.css").toExternalForm());
        resetCombat(controller);

        return root;
    }

    private void resetCombat(final GameController controller) {
        attackButton.setDisable(false);
        kill = true;

        // Resetta le barre della vita
        playerHealthBar.setProgress(controller.getPlayerLife() / 100);
        enemyHealthBar.setProgress(controller.getEnemyLifePoints() / 100);

        final Label playerHpLabel = new Label();
        final Label enemyHpLabel = new Label();

        // Aggiorna le etichette della vita
        playerHpLabel.setText(controller.getPlayerLife() + "HP");
        enemyHpLabel.setText(controller.getEnemyLifePoints() + "HP");
    }

    /**
     * Shows a popup dialog for the end of the game.
     * 
     * @param onClose callback to be executed when the dialog is closed.
     */
    private void popUp(final Runnable onClose) {
        final Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("GAME OVER");
        dialog.setHeaderText(null); // Rimuove il titolo predefinito

        // Imposta la dimensione della finestra
        dialog.getDialogPane().setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Testo grande per il messaggio di sconfitta
        final Label loseLabel = new Label("YOU LOSE THE GAME");
        loseLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: red;");

        // Aggiungiamo un ButtonType fittizio per abilitare la chiusura con la X
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Bottone "Leave"
        final Button btLeave = new Button("Leave");
        btLeave.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
        btLeave.setPrefSize(DIALOG_WIDTH, DIALOG_HEIGHT); // Aumenta le dimensioni del bottone

        // Azione del bottone per chiudere la finestra
        btLeave.setOnAction(event -> {
            LOGGER.info("Restart the game");
            dialog.setResult(null); // Imposta un risultato per chiudere la dialog
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
