package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * the trap view class.
 */
public class TrapView {
    private static final int SPACING = 20;
    private static final int BT_WIDTH = 400;
    private static final int BT_HEIGHT = 300;

    /**
     * Creates the scene for the Trap view.
     *
     * @param manager    the scene manager
     * @param controller the game controller
     * @return the created scene
     */
    public StackPane createScene(final SceneManager manager, final GameController controller) {
        final StackPane root = new StackPane();

        manager.setBackground(manager, root, controller.getBackgroundImage());

        final Label trapLabel = new Label("YOU TAKE A TRAP");
        trapLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");

        final Button takeButton = new Button("Take");
        takeButton.setStyle("-fx-font-size: 20px;");
        takeButton.setStyle(
                "-fx-font-size: 24px; "
                        + "-fx-font-weight: bold; "
                        + "-fx-text-fill: white; "
                        + "-fx-background-color: black; "
                        + "-fx-padding: 15px 30px;");

        takeButton.setMinWidth(BT_HEIGHT);
        takeButton.setMaxWidth(BT_WIDTH);

        final Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 20px;");
        exitButton.setDisable(true);
        exitButton.setOnAction(e -> manager.switchTo("main_floor_view"));
        exitButton.setStyle(
                "-fx-font-size: 24px; "
                        + "-fx-font-weight: bold; "
                        + "-fx-text-fill: white; "
                        + "-fx-background-color: black; "
                        + "-fx-padding: 15px 30px;");

        exitButton.setMinWidth(BT_HEIGHT);
        exitButton.setMaxWidth(BT_WIDTH);

        takeButton.setOnAction(e -> {
            final Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Life Points");

            controller.attackPlayer();

            final Label popupLabel = new Label("ACTUAL LIFE POINTS: " + controller.getPlayerLife());
            popupLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            final ButtonType closeButtonType = ButtonType.CLOSE;
            dialog.getDialogPane().getButtonTypes().add(closeButtonType);

            final VBox popupLayout = new VBox(SPACING, popupLabel);
            popupLayout.setAlignment(Pos.CENTER);
            popupLayout.setPadding(new Insets(SPACING));

            dialog.getDialogPane().setContent(popupLayout);

            dialog.setOnHidden(event -> {
                exitButton.setDisable(false);
                takeButton.setDisable(true);
            });

            dialog.showAndWait();
        });

        final VBox layout = new VBox(20, trapLabel, takeButton, exitButton);
        layout.setAlignment(Pos.CENTER);

        root.getChildren().add(layout);
        return root;
    }

}
