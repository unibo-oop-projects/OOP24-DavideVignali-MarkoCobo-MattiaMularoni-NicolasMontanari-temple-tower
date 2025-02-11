package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import it.unibo.templetower.controller.GameController;
import it.unibo.templetower.controller.GameControllerImpl;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Manages the different scenes in the game application.
 * This class is responsible for creating, storing, and switching between different game scenes.
 */
public class SceneManager {
    private final Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();
    private final GameController controller;

    /**
     * Creates a new SceneManager.
     *
     * @param stage the primary stage of the JavaFX application
     */
    public SceneManager(final Stage stage) {
        this.stage = stage;
        this.controller = new GameControllerImpl();
        initializeScenes();
    }

    /**
     * Initializes all game scenes.
     */
    private void initializeScenes() {
        scenes.put("difficulty_menu", new DifficultyMenu().createScene(this, controller));
        scenes.put("main_floor_view", new MainFloorView().createScene(this, controller));
        scenes.put("combat_view", new CombatView().createScene(this, controller));
        scenes.put("treasure_view", new TreasureView().createScene(this, controller));
        scenes.put("stairs_view", new StairsView().createScene(this, controller));
        try {
            scenes.put("enter_menu", new EnterMenu().createScene(this));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            scenes.put("home", new Home().createScene(this));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches the current scene to the specified scene.
     * This method can be overridden by subclasses to provide custom scene switching behavior.
     * When overriding, ensure that the scene exists in the scenes map and properly apply CSS styles.
     *
     * @param sceneName the name of the scene to switch to
     * @throws IllegalArgumentException if the specified scene name is not found
     */
    public void switchTo(final String sceneName) {
        Scene scene = scenes.get(sceneName);
        String css = this.getClass().getResource("/css/main.css").toExternalForm(); 
        if (scene != null) {
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();
        } else {
            throw new IllegalArgumentException("Scene " + sceneName + " not found");
        }
    }
}
