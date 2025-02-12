package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class TrapView {
    
    public Scene createScene(SceneManager manager, GameController controller){
        // Contenitore principale per gestire il passaggio alla modalità video
        StackPane root = new StackPane();
        return new Scene(root, 800, 600);
    }
}
