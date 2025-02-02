package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class CombatView {
    public Scene createScene(SceneManager manager, GameController controller) {
        // Creazione del layout principale
        StackPane root = new StackPane();
        root.getStyleClass().add("root"); // Apply root style

        // Dichiarazione dello sfondo
        ImageView backgroundView = new ImageView();

        // Aggiunta dello sfondo
        try {
            Image backgroundImage = new Image(getClass().getResource("/Images/combat_room.jpg").toExternalForm());
            backgroundView.setImage(backgroundImage);
            backgroundView.setFitWidth(800);
            backgroundView.setFitHeight(600);
            backgroundView.setPreserveRatio(false); // Forza l'immagine a coprire tutta l'area
            root.getChildren().add(backgroundView);
        } catch (Exception e) {
            Label errorLabel = new Label("Background image not found.");
            errorLabel.getStyleClass().add("label"); // Apply label style
            root.getChildren().add(errorLabel);
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/css/Combat.css").toExternalForm()); // Load CSS
            return scene; // Mostra solo l'errore se l'immagine non viene caricata
        }

        // Layout dei contenuti
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);

        // Immagini degli omini
        HBox charactersBox = new HBox(100);
        charactersBox.setAlignment(Pos.CENTER);

        ImageView playerImage = new ImageView(new Image(getClass().getResource("/Images/player.png").toExternalForm()));
        ImageView enemyImage = new ImageView(new Image(getClass().getResource("/Images/enemy.png").toExternalForm()));

        // Aumentare la dimensione delle immagini
        playerImage.setFitWidth(150);
        playerImage.setFitHeight(150);
        enemyImage.setFitWidth(150);
        enemyImage.setFitHeight(150);

        charactersBox.getChildren().addAll(playerImage, enemyImage);

        // Layout dei contenuti modificato per posizionare le barre della vita in basso
        VBox rootBox = new VBox(20);
        rootBox.setAlignment(Pos.CENTER);
        rootBox.getChildren().addAll(charactersBox, contentBox);

        // Barre della vita
        HBox healthBarsBox = new HBox(50);
        healthBarsBox.setAlignment(Pos.BOTTOM_CENTER);

        VBox playerHealthBox = new VBox(5);
        Label playerHpLabel = new Label("100 HP");
        playerHpLabel.getStyleClass().add("label"); // Apply label style
        ProgressBar playerHealthBar = new ProgressBar(1.0);
        playerHealthBar.getStyleClass().add("health-bar-player"); // Apply player health bar style
        playerHealthBar.setPrefWidth(200);
        playerHealthBox.getChildren().addAll(playerHpLabel, playerHealthBar);

        VBox enemyHealthBox = new VBox(5);
        Label enemyHpLabel = new Label("100 HP");
        enemyHpLabel.getStyleClass().add("label"); // Apply label style
        ProgressBar enemyHealthBar = new ProgressBar(1.0);
        enemyHealthBar.getStyleClass().add("health-bar-enemy"); // Apply enemy health bar style
        enemyHealthBar.setPrefWidth(200);
        enemyHealthBox.getChildren().addAll(enemyHpLabel, enemyHealthBar);

        healthBarsBox.getChildren().addAll(playerHealthBox, enemyHealthBox);
        rootBox.getChildren().add(healthBarsBox);

        // Pulsante di attacco
        Button attackButton = new Button("Attack!");
        attackButton.getStyleClass().add("button"); // Apply button style
        attackButton.setOnAction(event -> {
            // Animazione di attacco
            Timeline timeline = new Timeline();
            double distance = (enemyImage.getLayoutX() - playerImage.getLayoutX()) - 30; // Move closer to enemy
            KeyValue kv = new KeyValue(playerImage.translateXProperty(), distance);
            KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(e -> {
                // Simula la riduzione dei punti vita
                double playerHealth = playerHealthBar.getProgress() - 0.1;
                double enemyHealth = enemyHealthBar.getProgress() - 0.15;

                if (playerHealth < 0) playerHealth = 0;
                if (enemyHealth < 0) enemyHealth = 0;

                playerHealthBar.setProgress(playerHealth);
                enemyHealthBar.setProgress(enemyHealth);

                playerHpLabel.setText((int) (playerHealth * 100) + " HP");
                enemyHpLabel.setText((int) (enemyHealth * 100) + " HP");

                if (playerHealth <= 0) {
                    attackButton.setDisable(true);
                    //manager.showMessage("Player defeated!");
                } else if (enemyHealth <= 0) {
                    attackButton.setDisable(true);
                    //manager.showMessage("Enemy defeated!");
                }

                // Reset posizione player
                playerImage.setTranslateX(0);
            });
            timeline.play();
        });

        // Costruisci il layout principale
        contentBox.getChildren().addAll(charactersBox, healthBarsBox, attackButton);

        // Posiziona i contenuti sopra lo sfondo
        root.getChildren().add(rootBox);

        // Rendi tutto responsivo
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/Combat.css").toExternalForm()); // Load CSS

        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            double newWidth = newVal.doubleValue();
            backgroundView.setFitWidth(newWidth);
        });

        root.heightProperty().addListener((obs, oldVal, newVal) -> {
            double newHeight = newVal.doubleValue();
            backgroundView.setFitHeight(newHeight);
        });

        return scene;
    }
}
