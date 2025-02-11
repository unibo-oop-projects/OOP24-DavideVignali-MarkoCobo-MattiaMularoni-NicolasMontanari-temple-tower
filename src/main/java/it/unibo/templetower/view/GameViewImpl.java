package it.unibo.templetower.view;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class that initializes and manages the game's user interface.
 * This class is responsible for starting the JavaFX application and setting up
 * the initial scene.
 */
public final class GameViewImpl extends Application {
    /**
     * Starts the JavaFX application by initializing the scene manager and
     * switching to the home scene.
     *
     * @param primaryStage the primary stage for this application
     * @throws FileNotFoundException if required scene files cannot be found
     */
    @Override
    public void start(final Stage primaryStage) throws FileNotFoundException {
        SceneManager manager = new SceneManager(primaryStage);
        manager.switchTo("home");
    }
}
