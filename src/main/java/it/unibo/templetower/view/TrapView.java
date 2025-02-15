package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * {@inheritDoc}.
 */
public class TrapView {
    private static final int SPACING = 20;

    /**
     * Creates the scene for the Trap view.
     *
     * @param manager    the scene manager
     * @param controller the game controller
     * @return the created scene
     */
    public StackPane createScene(final SceneManager manager, final GameController controller) {
        final StackPane root = new StackPane();

        final Label trapLabel = new Label("YOU TAKE A TRAP");
        trapLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");

        controller.attackPlayer();

        final Label lifeLabel = new Label("ACTUAL LIFE POINTS: " + controller.getPlayerLife());
        lifeLabel.setStyle("-fx-font-size: 36px;");

        final Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 20px;");
        exitButton.setOnAction(e -> manager.switchTo("main_floor_view"));
        exitButton.getStyleClass().add("button");

        final VBox layout = new VBox(SPACING, trapLabel, lifeLabel, exitButton);
        layout.setAlignment(Pos.CENTER);

        root.getChildren().add(layout);
        return root;
    }
}
