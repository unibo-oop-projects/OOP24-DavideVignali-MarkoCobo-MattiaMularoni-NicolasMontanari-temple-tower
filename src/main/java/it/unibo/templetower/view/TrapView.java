package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.scene.layout.StackPane;

/**
* {@inheritDoc}.
*/
public class TrapView {

    /**
     * 
     * Creates the scene for the Trap view.
     *
     * @param manager    the scene manager
     * @param controller the game controller
     * @return the created scene
     */
    public StackPane createScene(final StageManager manager, final GameController controller) {
        // Contenitore principale per gestire il passaggio alla modalità video
        return new StackPane();
    }
}
