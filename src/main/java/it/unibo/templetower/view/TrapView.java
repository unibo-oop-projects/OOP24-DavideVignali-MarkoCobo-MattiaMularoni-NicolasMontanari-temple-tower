package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

/**
* {@inheritDoc}.
*/
public class TrapView {
    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;

    /**
     * 
     * Creates the scene for the Trap view.
     *
     * @param manager    the scene manager
     * @param controller the game controller
     * @return the created scene
     */
    public Scene createScene(final SceneManager manager, final GameController controller) {
        // Contenitore principale per gestire il passaggio alla modalità video
        final StackPane root = new StackPane();
        return new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    }
}
