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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Manages the different panes in the game application.
 * This class is responsible for creating, storing, and switching between
 * different game panes.
 */
public final class StageManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(StageManager.class);
    private static final double INITIAL_HEIGHT = Screen.getPrimary().getBounds().getHeight();
    private static final double INITIAL_WIDTH = Screen.getPrimary().getBounds().getWidth();
    private static final String CSS_PATH = "/css/main.css";

    private final GameController controller;
    private final Map<String, Pane> panes = new HashMap<>();
    private final Stage stage;
    private final Scene scene;

    /**
     * Creates a new SceneManager.
     *
     * @param stage the primary stage of the JavaFX application
     */
    public StageManager(final Stage stage) {
        this.stage = Objects.requireNonNull(stage, "Stage cannot be null");
        this.controller = new GameControllerImpl();
        this.scene = new Scene(new StackPane(), INITIAL_WIDTH, INITIAL_HEIGHT);
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        initializePanes();
    }

    /**
     * Initializes all game panes.
     */
    private void initializePanes() {
        try {
            panes.put("difficulty_menu", new DifficultyMenu().createScene(this, controller));
            panes.put("main_floor_view", new MainFloorView().createScene(this, controller));
            panes.put("combat_view", new CombatView().createScene(this, controller));
            panes.put("treasure_view", new TreasureView().createScene(this, controller));
            panes.put("stairs_view", new StairsView().createScene(this, controller));
            panes.put("enter_menu", new EnterMenu().createScene(this));
            panes.put("home", new Home().createScene(this));
        } catch (IOException e) {
            LOGGER.error("Failed to initialize panes: " + e.getMessage());
            throw new IllegalStateException("Failed to initialize panes", e);
        }
    }

    /**
     * Switches the current scene to the specified scene.
     *
     * @param sceneName the name of the scene to switch to
     * @throws IllegalArgumentException if the specified scene name is not found
     */
    public void switchTo(final String sceneName) {
        Pane pane = panes.get(sceneName);
        if ("combat_view".equals(sceneName)) {
            pane = new CombatView().createScene(this, controller);
        }
        if (pane == null) {
            throw new IllegalArgumentException("Scene " + sceneName + " not found");
        }
        applyStylesheet(pane);
        updateStage(pane);
    }

    /**
     * Applies the CSS stylesheet to the scene.
     *
     * @param pane the scene to which the stylesheet should be applied
     */
    private void applyStylesheet(final Pane pane) {
        final URL cssResource = getClass().getResource(CSS_PATH);
        if (cssResource != null) {
            pane.getStylesheets().add(cssResource.toExternalForm());
        } else {
            LOGGER.warn("CSS file not found at path: " + CSS_PATH);
        }
    }

    /**
     * Applies the screen size of the previous scene and sets
     * the new scene on stage.
     *
     * @param pane the new scene to be set
     */
    private void updateStage(final Pane pane) {
        scene.setRoot(pane);
        stage.show();
    }
}
