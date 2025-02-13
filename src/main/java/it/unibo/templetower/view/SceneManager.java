package it.unibo.templetower.view;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.templetower.controller.GameController;
import it.unibo.templetower.controller.GameControllerImpl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Manages the different scenes in the game application.
 * This class is responsible for creating, storing, and switching between
 * different game scenes.
 */
public final class SceneManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneManager.class);
    private static final double INITIAL_HEIGHT = Screen.getPrimary().getBounds().getHeight();
    private static final double INITIAL_WIDTH = Screen.getPrimary().getBounds().getWidth();
    private static final String CSS_PATH = "/css/main.css";

    private final GameController controller;
    private final Map<String, Scene> scenes = new HashMap<>();
    private final Stage stage;
    private Scene currentScene;

    /**
     * Creates a new SceneManager.
     *
     * @param stage the primary stage of the JavaFX application
     */
    public SceneManager(final Stage stage) {
        this.stage = Objects.requireNonNull(stage, "Stage cannot be null");
        this.controller = new GameControllerImpl();
        this.currentScene = new Scene(new StackPane(), INITIAL_WIDTH, INITIAL_HEIGHT);
        initializeScenes();
    }

    /**
     * Initializes all game scenes.
     */
    private void initializeScenes() {
        try {
            scenes.put("difficulty_menu", new DifficultyMenu().createScene(this, controller));
            scenes.put("main_floor_view", new MainFloorView().createScene(this, controller));
            scenes.put("combat_view", new CombatView().createScene(this, controller));
            scenes.put("treasure_view", new TreasureView().createScene(this, controller));
            scenes.put("stairs_view", new StairsView().createScene(this, controller));
            scenes.put("enter_menu", new EnterMenu().createScene(this));
            scenes.put("home", new Home().createScene(this));
        } catch (IOException e) {
            LOGGER.error("Failed to initialize scenes: " + e.getMessage());
            throw new IllegalStateException("Failed to initialize scenes", e);
        }
    }

    /**
     * Switches the current scene to the specified scene.
     *
     * @param sceneName the name of the scene to switch to
     * @throws IllegalArgumentException if the specified scene name is not found
     */
    public void switchTo(final String sceneName) {
        Scene scene = scenes.get(sceneName);
        if ("combat_view".equals(sceneName)) {
            scene = new CombatView().createScene(this, controller);
        }
        if (scene == null) {
            throw new IllegalArgumentException("Scene " + sceneName + " not found");
        }
        applyStylesheet(scene);
        updateStage(scene);
    }

    /**
     * Applies the CSS stylesheet to the scene.
     *
     * @param scene the scene to which the stylesheet should be applied
     */
    private void applyStylesheet(final Scene scene) {
        final URL cssResource = getClass().getResource(CSS_PATH);
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        } else {
            LOGGER.warn("CSS file not found at path: " + CSS_PATH);
        }
    }

    /**
     * Applies the screen size of the previous scene and sets
     * the new scene on stage.
     *
     * @param scene the new scene to be set
     */
    private void updateStage(final Scene scene) {
        stage.setScene(scene);
        stage.setWidth(currentScene.getWidth());
        stage.setHeight(currentScene.getHeight());
        currentScene = scene;
        stage.show();
    }
}
