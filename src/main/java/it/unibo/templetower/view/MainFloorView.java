package it.unibo.templetower.view;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainFloorView {
    public Scene createScene(SceneManager manager) {
        VBox root = new VBox(10);

        Button settingsButton = new Button("Go to Settings");
        settingsButton.setOnAction(e -> manager.switchTo("combat_view"));

        root.getChildren().addAll(settingsButton);
        root.getStyleClass().add("dashboard-view");

        return new Scene(root, 400, 300);
    }
}