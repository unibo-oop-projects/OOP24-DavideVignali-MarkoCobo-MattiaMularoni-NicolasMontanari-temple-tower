package it.unibo.templetower.view;

import java.io.File;

import it.unibo.templetower.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * the trap view class.
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

        final String bgImage = controller.getBackgroundImage();

        final Image backgroundImage;
        try {
            final File file = new File(bgImage);
            backgroundImage = new Image(file.toURI().toString());
            final ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setPreserveRatio(false);
            backgroundView.fitWidthProperty().bind(root.widthProperty());
            backgroundView.fitHeightProperty().bind(root.heightProperty());
            root.getChildren().add(backgroundView);
        } catch (IllegalArgumentException e) {
            final Label errorLabel = new Label("Background image not found.");
            errorLabel.getStyleClass().add("label");
            root.getChildren().add(errorLabel);
        }

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
