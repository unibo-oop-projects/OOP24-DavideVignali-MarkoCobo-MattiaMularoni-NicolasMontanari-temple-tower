package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import it.unibo.templetower.controller.GameController;
import it.unibo.templetower.controller.GameControllerImpl;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the different scenes in the game application.
 * This class is responsible for creating, storing, and switching between different game scenes.
 */
public final class SceneManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneManager.class);
    private final Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();
    private final GameController controller;

    /**
     * Creates a new SceneManager.
     *
     * @param stage the primary stage of the JavaFX application
     */
    public SceneManager(final Stage stage) {
        this.stage = Objects.requireNonNull(stage, "Stage cannot be null");
        this.controller = new GameControllerImpl();
        initializeScenes();
    }

    /**
     * Initializes all game scenes.
     */
    private void initializeScenes() {
    private void initializeScenes()  {
        scenes.put("difficulty_menu", new DifficultyMenu().createScene(this, controller));
        scenes.put("main_floor_view", new MainFloorView().createScene(this, controller));
        scenes.put("treasure_view", new TreasureView().createScene(this, controller));
        scenes.put("stairs_view", new StairsView().createScene(this, controller));
        try {
            scenes.put("difficulty_menu", new DifficultyMenu().createScene(this, controller));
            scenes.put("main_floor_view", new MainFloorView().createScene(this, controller));
            scenes.put("combat_view", new CombatView().createScene(this, controller));
            scenes.put("treasure_view", new TreasureView().createScene(this, controller));
            scenes.put("stairs_view", new StairsView().createScene(this, controller));
            scenes.put("enter_menu", new EnterMenu().createScene(this));
            scenes.put("home", new Home().createScene(this));
        } catch (FileNotFoundException e) {
            LOGGER.error("Failed to initialize scenes: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to initialize scenes", e);
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
        final Scene scene = scenes.get(sceneName);
        if (scene == null) {
    public void switchTo(String sceneName) {
        Scene scene;
        if(sceneName == "combat_view"){ // ogni volta che entriamo in una enemy room, aggiorna i dati dell'enemy
            scene = new CombatView().createScene(this, controller);
        }else{
            scene = scenes.get(sceneName);
        }
        String css = this.getClass().getResource("/css/main.css").toExternalForm(); 
        if (scene != null) {
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();
        } else {
            throw new IllegalArgumentException("Scene " + sceneName + " not found");
        }

        final URL cssResource = SceneManager.class.getResource("/css/main.css");
        if (cssResource == null) {
            LOGGER.warn("CSS file not found: /css/main.css");
        } else {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }

        stage.setScene(scene);
        stage.show();
    }
}
