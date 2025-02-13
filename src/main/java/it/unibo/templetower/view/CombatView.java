package it.unibo.templetower.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.templetower.controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Represents the combat view of the game where battles take place.
 * This class is responsible for creating and managing the combat scene.
 */
public final class CombatView {
    private static final int SPACING_LARGE = 20;
    private static final int SPACING_SMALL = 5;
    private static final int CHARACTER_SIZE = 150;
    private static final int BUTTON_WIDTH = 150;
    private static final int HEALTH_BAR_WIDTH = 200;
    private static final int ATTACK_DISTANCE = 30;
    private static final double INITIAL_HEALTH = 1.0;

    private static final Logger LOGGER = LoggerFactory.getLogger(CombatView.class);

    private ProgressBar playerHealthBar;
    private ProgressBar enemyHealthBar;
    private Button attackButton;
    private Button exitButton;

    /**
     * Creates and returns the combat scene with all necessary UI elements.
     * 
     * @param manager    the scene manager to handle scene transitions
     * @param controller the game controller to handle game logic
     * @return the created combat scene
     */
    public StackPane createScene(final StageManager manager, final GameController controller) {
        final StackPane root = new StackPane();
        root.getStyleClass().add("root");

        final ImageView backgroundView = new ImageView();
        try {
            final Image backgroundImage = new Image(getClass().getResource("/Images/combat_room.jpg").toExternalForm());
            backgroundView.setImage(backgroundImage);
            backgroundView.setPreserveRatio(false);
            backgroundView.setFitWidth(Region.USE_COMPUTED_SIZE);
            backgroundView.setFitHeight(Region.USE_COMPUTED_SIZE);

            root.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                backgroundView.setFitWidth(newWidth.doubleValue());
            });
            root.heightProperty().addListener((obs, oldHeight, newHeight) -> {
                backgroundView.setFitHeight(newHeight.doubleValue());
            });

            root.getChildren().add(backgroundView);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed to load background image: {}", e.getMessage());
            final Label errorLabel = new Label("Background image not found.");
            errorLabel.getStyleClass().add("label");
            root.getChildren().add(errorLabel);
            return root;
        }

        final VBox contentBox = new VBox(SPACING_LARGE);
        contentBox.setAlignment(Pos.BOTTOM_CENTER);

        final HBox charactersBox = new HBox(SPACING_LARGE);
        charactersBox.setAlignment(Pos.BOTTOM_CENTER);

        final ImageView playerImage = new ImageView(
                new Image(getClass().getResource("/Images/player.png").toExternalForm()));
        final ImageView enemyImage = new ImageView(
                new Image(getClass().getResource("/Images/enemy.png").toExternalForm()));

        playerImage.setFitWidth(CHARACTER_SIZE);
        playerImage.setFitHeight(CHARACTER_SIZE);
        enemyImage.setFitWidth(CHARACTER_SIZE);
        enemyImage.setFitHeight(CHARACTER_SIZE);

        // **Inizializziamo i pulsanti PRIMA del listener**
        attackButton = new Button("Attack!");
        attackButton.getStyleClass().add("button");

        exitButton = new Button("Exit");
        exitButton.getStyleClass().add("button");

        playerHealthBar = new ProgressBar(INITIAL_HEALTH);
        playerHealthBar.getStyleClass().add("health-bar-player");

        enemyHealthBar = new ProgressBar(controller.getEnemyLifePoints() / 10);
        enemyHealthBar.getStyleClass().add("health-bar-enemy");

        // **Listener per il ridimensionamento di immagini, bottoni e progress bar**
        root.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            final double scaleFactor = newWidth.doubleValue() / root.getWidth();
            final double newSize = CHARACTER_SIZE * scaleFactor;

            playerImage.setFitWidth(newSize);
            playerImage.setFitHeight(newSize);
            enemyImage.setFitWidth(newSize);
            enemyImage.setFitHeight(newSize);

            // Ridimensiona i bottoni e le progress bar
            final double newButtonWidth = BUTTON_WIDTH * scaleFactor;
            final double newProgressBarWidth = HEALTH_BAR_WIDTH * scaleFactor;

            attackButton.setPrefWidth(newButtonWidth);
            exitButton.setPrefWidth(newButtonWidth);

            playerHealthBar.setPrefWidth(newProgressBarWidth);
            enemyHealthBar.setPrefWidth(newProgressBarWidth);
        });

        charactersBox.getChildren().addAll(playerImage, enemyImage);

        final VBox rootBox = new VBox();
        rootBox.setAlignment(Pos.BOTTOM_CENTER);

        final BorderPane healthBarsPane = new BorderPane();
        healthBarsPane.setPadding(new Insets(10));

        final Label playerHpLabel = new Label(controller.getPlayerLife() + "HP");
        playerHpLabel.getStyleClass().add("label");
        final VBox playerHealthBox = new VBox(SPACING_SMALL, playerHpLabel, playerHealthBar);
        playerHealthBox.setAlignment(Pos.BOTTOM_LEFT);
        healthBarsPane.setLeft(playerHealthBox);

        final Label enemyHpLabel = new Label(controller.getEnemyLifePoints() + "HP");
        enemyHpLabel.getStyleClass().add("label");
        final VBox enemyHealthBox = new VBox(SPACING_SMALL, enemyHpLabel, enemyHealthBar);
        enemyHealthBox.setAlignment(Pos.BOTTOM_RIGHT);
        healthBarsPane.setRight(enemyHealthBox);

        exitButton.setOnAction(e -> {
            LOGGER.debug("Enemy life points: {}", controller.getEnemyLifePoints());
            manager.switchTo("main_floor_view");
        });

        attackButton.setOnAction(event -> {
            LOGGER.debug("Enemy life points: {}", controller.getEnemyLifePoints());
            final Timeline timeline = new Timeline();
            final double distance = enemyImage.getLayoutX() - playerImage.getLayoutX() - ATTACK_DISTANCE;
            final KeyValue kv = new KeyValue(playerImage.translateXProperty(), distance);
            final KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(e -> {
                controller.attackEnemy();

                // Attacca il player e ottieni il danno inflitto
                controller.attackPlayer();

                // Aggiorna le barre di salute
                double playerHealth = playerHealthBar.getProgress() - (controller.getPlayerLife() / 100);
                double enemyHealth = enemyHealthBar.getProgress() - (controller.getEnemyLifePoints() / 100);

                if (playerHealth < 0) {
                    playerHealth = 0;
                }
                if (enemyHealth < 0) {
                    enemyHealth = 0;
                }

                playerHealthBar.setProgress(playerHealth);
                enemyHealthBar.setProgress(enemyHealth);

                playerHpLabel.setText(controller.getPlayerLife() + "HP");
                enemyHpLabel.setText(controller.getEnemyLifePoints() + "HP");

                if (playerHealth <= 0 || enemyHealth <= 0) {
                    attackButton.setDisable(true);
                }

                playerImage.setTranslateX(0);
            });
            timeline.play();
        });

        final HBox buttonBox = new HBox(SPACING_LARGE, attackButton, exitButton);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        healthBarsPane.setBottom(buttonBox);

        rootBox.getChildren().addAll(charactersBox, healthBarsPane);
        root.getChildren().add(rootBox);

        root.getStylesheets().add(getClass().getResource("/css/Combat.css").toExternalForm());
        resetCombat(controller);

        return root;
    }

    private void resetCombat(final GameController controller) {
        attackButton.setDisable(false);

        // Resetta le barre della vita
        playerHealthBar.setProgress(controller.getPlayerLife() / 100);
        enemyHealthBar.setProgress(controller.getEnemyLifePoints() / 100);

        final Label playerHpLabel = new Label();
        final Label enemyHpLabel = new Label();

        // Aggiorna le etichette della vita
        playerHpLabel.setText(controller.getPlayerLife() + "HP");
        enemyHpLabel.setText(controller.getEnemyLifePoints() + "HP");
    }

}
