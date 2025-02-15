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
public final class CombatView {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int DIALOG_WIDTH = 250;
    private static final int DIALOG_HEIGHT = 80;
    private static final int VBOX = 50;
    private static final int CHARACTER_SIZE = 150;
    private static final int BUTTON_WIDTH = 150;
    private static final int HEALTH_BAR_WIDTH = 200;
    private static final String ZEROHP = "0HP";
    private static final int FIRE_START_X = 40;
    private static final int FIRE_START_Y = 200;
    private static final int DISTANCE_FROM_ENEMY = 30;
    private static final int DISTANCE_FROM_BOSS = 700;

    private static final Logger LOGGER = LoggerFactory.getLogger(CombatView.class);

    private ProgressBar playerHealthBar;
    private ProgressBar enemyHealthBar;
    private Button attackBt;
    private Button exitBt;
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
        final String bgImage;
        final ImageView backgroundView = new ImageView();
        try {
            if (!controller.isBossTime()) {
                bgImage = "/Images/combat_room.jpg";
            } else {
                bgImage = "/Images/final_arena.png";
            }
            final Image backgroundImage = new Image(getClass().getResource(bgImage).toExternalForm());
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

        final String playerImg;
        final String enemyImg;
        if (!controller.isBossTime()) {
            playerImg = "/Images/player.png";
            enemyImg = "/Images/enemy.png";
        } else {
            playerImg = "/Images/playerback.png";
            enemyImg = "/Images/boss.png";
        }

        final ImageView playerImage = new ImageView(new Image(getClass().getResource(playerImg).toExternalForm()));
        final ImageView enemyImage = new ImageView(new Image(getClass().getResource(enemyImg).toExternalForm()));

        playerImage.setFitWidth(CHARACTER_SIZE);
        playerImage.setFitHeight(CHARACTER_SIZE);
        enemyImage.setFitWidth(CHARACTER_SIZE);
        enemyImage.setFitHeight(CHARACTER_SIZE);

        attackBt = new Button("Attack!");
        attackBt.getStyleClass().add("button");

        exitBt = new Button("Exit");
        exitBt.getStyleClass().add("button");

        playerHealthBar = new ProgressBar(controller.getPlayerLife() / 100);
        playerHealthBar.getStyleClass().add("health-bar-player");

        enemyHealthBar = new ProgressBar(controller.getEnemyLifePoints() / 100);
        enemyHealthBar.getStyleClass().add("health-bar-enemy");

        root.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            final double scaleFactor = newWidth.doubleValue() / WINDOW_WIDTH;
            playerImage.setFitWidth(CHARACTER_SIZE * scaleFactor);
            playerImage.setFitHeight(CHARACTER_SIZE * scaleFactor);
            enemyImage.setFitWidth(CHARACTER_SIZE * scaleFactor);
            enemyImage.setFitHeight(CHARACTER_SIZE * scaleFactor);
            attackBt.setPrefWidth(BUTTON_WIDTH * scaleFactor);
            exitBt.setPrefWidth(BUTTON_WIDTH * scaleFactor);
            playerHealthBar.setPrefWidth(HEALTH_BAR_WIDTH * scaleFactor);
            enemyHealthBar.setPrefWidth(HEALTH_BAR_WIDTH * scaleFactor);
        });
        final HBox charactersBox = new HBox(20);
        final VBox rootBox = new VBox();
        rootBox.setAlignment(Pos.BOTTOM_CENTER);

        final VBox spriteContainer = new VBox(100);
        if (!controller.isBossTime()) {
            charactersBox.setAlignment(Pos.BOTTOM_CENTER);
            charactersBox.getChildren().addAll(playerImage, enemyImage);
        } else {
            rootBox.setAlignment(Pos.BOTTOM_CENTER);
            spriteContainer.getChildren().addAll(enemyImage, playerImage);
            spriteContainer.setStyle("-fx-alignment: center;");
            rootBox.getChildren().add(spriteContainer);
        }

        final BorderPane healthBarsPane = new BorderPane();
        healthBarsPane.setPadding(new Insets(10));

        final Label playerHpLabel = new Label(controller.getPlayerLife() + "HP");
        playerHpLabel.getStyleClass().add("label");
        final VBox playerHealthBox = new VBox(5, playerHpLabel, playerHealthBar);
        playerHealthBox.setAlignment(Pos.BOTTOM_LEFT);
        healthBarsPane.setLeft(playerHealthBox);

        final Label enemyHpLabel = new Label(controller.getEnemyLifePoints() + "HP");
        enemyHpLabel.getStyleClass().add("label");
        final VBox enemyHealthBox = new VBox(5, enemyHpLabel, enemyHealthBar);
        enemyHealthBox.setAlignment(Pos.BOTTOM_RIGHT);
        healthBarsPane.setRight(enemyHealthBox);

        exitBt.setOnAction(_ -> {
            LOGGER.debug("Enemy life points: {}", controller.getEnemyLifePoints());
            manager.switchTo("main_floor_view");
        });

        attackBt.setOnAction(_ -> {
            LOGGER.debug("Enemy life points: {}", controller.getEnemyLifePoints());

            final ImageView attackImage = new ImageView(new Image(getClass().getResourceAsStream("/Images/flame.gif")));
            attackImage.setFitWidth(100);
            attackImage.setFitHeight(100);
            attackImage.setLayoutX(playerImage.getLayoutX() + FIRE_START_X);
            attackImage.setLayoutY(playerImage.getLayoutY() + FIRE_START_Y);

            ((Pane) playerImage.getParent()).getChildren().add(attackImage);

            final double distance;
            final KeyValue kv;
            final KeyFrame kf;

            final Timeline timeline = new Timeline();
            if (controller.isBossTime()) {
                distance = enemyImage.getLayoutX() - playerImage.getLayoutX() - DISTANCE_FROM_BOSS;
                kv = new KeyValue(attackImage.translateYProperty(), distance);
                kf = new KeyFrame(Duration.seconds(1), kv);
            } else {
                ((Pane) attackImage.getParent()).getChildren().remove(attackImage);
                distance = enemyImage.getLayoutX() - playerImage.getLayoutX() - DISTANCE_FROM_ENEMY;
                kv = new KeyValue(playerImage.translateXProperty(), distance);
                kf = new KeyFrame(Duration.seconds(0.5), kv);
            }

            timeline.getKeyFrames().add(kf);

            timeline.setOnFinished(_ -> {
                if (controller.isBossTime()) {
                    ((Pane) attackImage.getParent()).getChildren().remove(attackImage);
                } 
                controller.attackEnemy();

                final PauseTransition pause = new PauseTransition(Duration.millis(200));
                pause.setOnFinished(_ -> {
                    Platform.runLater(() -> {
                        if (controller.getEnemyLifePoints() <= 0) {
                            enemyHpLabel.setText(ZEROHP);
                            attackBt.setDisable(true);
                            controller.resetPlayerLife();
                            enemyHealthBar.setProgress(0 / 100.0);
                            kill = false;
                        } else if (kill) {
                            controller.attackPlayer();

                            final PauseTransition pause2 = new PauseTransition(Duration.millis(100));
                            pause2.setOnFinished(_ -> {
                                playerHealthBar.setProgress(controller.getPlayerLife() / 100.0);
                                playerHpLabel.setText(controller.getPlayerLife() + "HP");

                                if (controller.getPlayerLife() <= 0) {
                                    attackBt.setDisable(true);
                                    playerHpLabel.setText(ZEROHP);
                                    controller.resetGame();
                                    popUp(() -> manager.switchTo("home"));
                                }
                            });
                            pause2.play();
                        }

                        playerHealthBar.setProgress(controller.getPlayerLife() / 100.0);
                        enemyHealthBar.setProgress(controller.getEnemyLifePoints() / 100.0);

                        playerHpLabel.setText(controller.getPlayerLife() + "HP");
                        enemyHpLabel.setText(controller.getEnemyLifePoints() + "HP");

                        if (controller.getEnemyLifePoints() <= 0) {
                            enemyHealthBar.setProgress(0 / 100.0);
                            enemyHpLabel.setText(ZEROHP);
                            attackBt.setDisable(true);
                            if (controller.getPlayerLife() < 100) {
                                controller.resetPlayerLife();
                            }
                        } else if (controller.getPlayerLife() <= 0) {
                            playerHealthBar.setProgress(0 / 100.0);
                            attackBt.setDisable(true);
                            playerHpLabel.setText(ZEROHP);
                            controller.resetGame();
                            Platform.runLater(() -> popUp(() -> manager.switchTo("home")));
                        }

                        playerImage.setTranslateX(0);
                    });
                });
                pause.play();
            });

            timeline.play();
        });

        final HBox buttonBox = new HBox(20, attackBt, exitBt);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        healthBarsPane.setBottom(buttonBox);

        final VBox topBox = new VBox();
        topBox.setAlignment(Pos.TOP_RIGHT);

        final Button changeWeapon = new Button("CHANGE WEAPON");
        changeWeapon.getStyleClass().add("button");
        final HBox highBox = new HBox(changeWeapon);
        highBox.setAlignment(Pos.TOP_RIGHT);
        topBox.getChildren().add(highBox);

        changeWeapon.setOnAction(_ -> {
            manager.switchTo("select_weapon_view");
        });

        if (!controller.isBossTime()) {
            rootBox.getChildren().addAll(charactersBox, healthBarsPane, topBox);
        } else {
            rootBox.getChildren().addAll(healthBarsPane, topBox);
        }
        root.getChildren().add(rootBox);

        root.getStylesheets().add(getClass().getResource("/css/Combat.css").toExternalForm());
        resetCombat(controller);

        return root;
    }

    private void resetCombat(final GameController controller) {
        attackBt.setDisable(false);
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

        dialog.getDialogPane().setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        final Label loseLabel = new Label("YOU LOSE THE GAME");
        loseLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: red;");

        // Aggiungiamo un ButtonType fittizio per abilitare la chiusura con la X
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Bottone "Leave"
        final Button btLeave = new Button("Leave");
        btLeave.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
        btLeave.setPrefSize(DIALOG_WIDTH, DIALOG_HEIGHT); // Aumenta le dimensioni del bottone

        // Azione del bottone per chiudere la finestra
        btLeave.setOnAction(_ -> {
            LOGGER.info("Restart the game");
            dialog.setResult(null); // Imposta un risultato per chiudere la dialog
            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        final HBox btContainer = new HBox(btLeave);
        btContainer.setAlignment(Pos.CENTER);

        // Contenitore principale con testo e bottone
        final VBox layout = new VBox(VBOX, loseLabel, btContainer);
        layout.setAlignment(Pos.CENTER);

        // Imposta il contenuto della finestra
        dialog.getDialogPane().setContent(layout);

        // Permette la chiusura con la X
        dialog.setOnCloseRequest(_ -> {
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
