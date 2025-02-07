package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

public class CombatView {
    private ProgressBar playerHealthBar;
    private ProgressBar enemyHealthBar;
    private Button attackButton;
    private Button exitButton;

    public Scene createScene(SceneManager manager, GameController controller) {
        StackPane root = new StackPane();
        root.getStyleClass().add("root");

        ImageView backgroundView = new ImageView();
        try {
            Image backgroundImage = new Image(getClass().getResource("/Images/combat_room.jpg").toExternalForm());
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
        } catch (Exception e) {
            Label errorLabel = new Label("Background image not found.");
            errorLabel.getStyleClass().add("label");
            root.getChildren().add(errorLabel);
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/css/Combat.css").toExternalForm());
            return scene;
        }

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.BOTTOM_CENTER);

        HBox charactersBox = new HBox(100);
        charactersBox.setAlignment(Pos.BOTTOM_CENTER);

        ImageView playerImage = new ImageView(new Image(getClass().getResource("/Images/player.png").toExternalForm()));
        ImageView enemyImage = new ImageView(new Image(getClass().getResource("/Images/enemy.png").toExternalForm()));

        double initialSize = 150;
        playerImage.setFitWidth(initialSize);
        playerImage.setFitHeight(initialSize);
        enemyImage.setFitWidth(initialSize);
        enemyImage.setFitHeight(initialSize);

        // **Inizializziamo i pulsanti PRIMA del listener**
        attackButton = new Button("Attack!");
        attackButton.getStyleClass().add("button");

        exitButton = new Button("Exit");
        exitButton.getStyleClass().add("button");

        playerHealthBar = new ProgressBar(1.0);
        playerHealthBar.getStyleClass().add("health-bar-player");

        enemyHealthBar = new ProgressBar(1.0);
        enemyHealthBar.getStyleClass().add("health-bar-enemy");

        // **Listener per il ridimensionamento di immagini, bottoni e progress bar**
        root.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double scaleFactor = newWidth.doubleValue() / 800;
            double newSize = initialSize * scaleFactor;

            playerImage.setFitWidth(newSize);
            playerImage.setFitHeight(newSize);
            enemyImage.setFitWidth(newSize);
            enemyImage.setFitHeight(newSize);

            // Ridimensiona i bottoni e le progress bar
            double newButtonWidth = 150 * scaleFactor;
            double newProgressBarWidth = 200 * scaleFactor;

            attackButton.setPrefWidth(newButtonWidth);
            exitButton.setPrefWidth(newButtonWidth);

            playerHealthBar.setPrefWidth(newProgressBarWidth);
            enemyHealthBar.setPrefWidth(newProgressBarWidth);
        });

        charactersBox.getChildren().addAll(playerImage, enemyImage);

        VBox rootBox = new VBox();
        rootBox.setAlignment(Pos.BOTTOM_CENTER);

        BorderPane healthBarsPane = new BorderPane();
        healthBarsPane.setPadding(new Insets(10));

        Label playerHpLabel = new Label("100 HP");
        playerHpLabel.getStyleClass().add("label");
        VBox playerHealthBox = new VBox(5, playerHpLabel, playerHealthBar);
        playerHealthBox.setAlignment(Pos.BOTTOM_LEFT);
        healthBarsPane.setLeft(playerHealthBox);

        Label enemyHpLabel = new Label("100 HP");
        enemyHpLabel.getStyleClass().add("label");
        VBox enemyHealthBox = new VBox(5, enemyHpLabel, enemyHealthBar);
        enemyHealthBox.setAlignment(Pos.BOTTOM_RIGHT);
        healthBarsPane.setRight(enemyHealthBox);

        attackButton.setOnAction(event -> {
            Timeline timeline = new Timeline();
            double distance = (enemyImage.getLayoutX() - playerImage.getLayoutX()) - 30;
            KeyValue kv = new KeyValue(playerImage.translateXProperty(), distance);
            KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(e -> {
                double playerHealth = playerHealthBar.getProgress() - 0.1;
                double enemyHealth = enemyHealthBar.getProgress() - 0.15;

                controller.attackEnemy();

                if (playerHealth < 0) playerHealth = 0;
                if (enemyHealth < 0) enemyHealth = 0;

                playerHealthBar.setProgress(playerHealth);
                enemyHealthBar.setProgress(enemyHealth);

                playerHpLabel.setText((int) (playerHealth * 100) + " HP");
                enemyHpLabel.setText((int) (enemyHealth * 100) + " HP");

                if (playerHealth <= 0 || enemyHealth <= 0) {
                    attackButton.setDisable(true);
                }

                playerImage.setTranslateX(0);
            });
            timeline.play();
        });

        HBox buttonBox = new HBox(20, attackButton, exitButton);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        healthBarsPane.setBottom(buttonBox);

        rootBox.getChildren().addAll(charactersBox, healthBarsPane);
        root.getChildren().add(rootBox);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/Combat.css").toExternalForm());

        return scene;
    }
}




