package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Select weapon view class.
 */
public class SelectWeaponView {
    private static final int VBOX_SPACING = 20;
    private static final int BT_WIDTH = 600;
    private static final int BT_HEIGHT = 300;

    /**
     * Creates and returns the change weapon scene with all necessary UI elements.
     * 
     * @param manager    the scene manager to handle scene transitions
     * @param controller the game controller
     * @return the created change weapon scene
     */
    public StackPane createScene(final SceneManager manager, final GameController controller) {
        final StackPane root = new StackPane();
        final VBox vbox = new VBox(VBOX_SPACING);
        vbox.setAlignment(Pos.CENTER);
        final Button weapon1;
        final Button weapon2;
        final Button weapon3;

        manager.setBackground(manager, root, controller.getBackgroundImage());

        final Label titleLabel = new Label("Select Weapon to USE");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        vbox.getChildren().add(titleLabel);

        if (!controller.getPlayerWeapons().isEmpty()) {
            weapon1 = new Button(controller.getPlayerWeapons().get(0).name() + " - Damage: "
                    + controller.getPlayerWeapons().get(0).attack().getY());
            weapon1.setOnAction(e -> controller.changeWeaponIndex(0));
            styleWeaponButton(weapon1);
            vbox.getChildren().add(weapon1);
        }

        if (controller.getPlayerWeapons().size() >= 2) {
            weapon2 = new Button(controller.getPlayerWeapons().get(1).name() + " - Damage: "
                    + controller.getPlayerWeapons().get(1).attack().getY());
            weapon2.setOnAction(e -> controller.changeWeaponIndex(1));
            styleWeaponButton(weapon2);
            vbox.getChildren().add(weapon2);
        }

        if (controller.getPlayerWeapons().size() == 3) {
            weapon3 = new Button(controller.getPlayerWeapons().get(2).name() + " - Damage: "
                    + controller.getPlayerWeapons().get(2).attack().getY());
            weapon3.setOnAction(e -> controller.changeWeaponIndex(2));
            styleWeaponButton(weapon3);
            vbox.getChildren().add(weapon3);
        }

        final Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");

        backButton.setOnAction(e -> manager.switchTo("combat_view")); 
        styleWeaponButton(backButton);

        vbox.getChildren().add(backButton);

        root.getChildren().add(vbox);

        return root;
    }

    /**
     * Applica lo stile ai bottoni delle armi.
     * 
     * @param button il pulsante da stilizzare
     */
    private void styleWeaponButton(final Button button) {
        button.setStyle(
                "-fx-font-size: 24px; "
                        + "-fx-font-weight: bold; "
                        + "-fx-text-fill: white; "
                        + "-fx-background-color: black; "
                        + "-fx-padding: 15px 30px;");
        button.setMinWidth(BT_HEIGHT);
        button.setMaxWidth(BT_WIDTH);
        button.setWrapText(true);
    }
}
