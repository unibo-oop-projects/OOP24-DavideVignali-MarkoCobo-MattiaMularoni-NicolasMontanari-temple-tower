package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.templetower.controller.GameController;
import it.unibo.templetower.controller.GameControllerImpl;
import it.unibo.templetower.controller.GameDataManagerImpl;
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
public final class SceneManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneManager.class);
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
    public SceneManager(final Stage stage) {
        this.stage = Objects.requireNonNull(stage, "Stage cannot be null");
        this.controller = new GameControllerImpl();
        this.scene = new Scene(new StackPane(), INITIAL_WIDTH, INITIAL_HEIGHT);
        stage.setScene(scene);
        initializeMenu();
    }

    private void initializeMenu() {
        try {
            panes.put("difficulty_menu", new DifficultyMenu().createScene(this, controller));
            panes.put("enter_menu", new EnterMenu().createScene(this));
            panes.put("settings_menu", new SettingsMenu().createScene(this));
            panes.put("home", new Home().createScene(this));
            panes.put("modding_menu", new ModdingMenuView().createScene(this)); // Add modding menu scene
        } catch (FileNotFoundException e) {
            LOGGER.error("Failed to initialize scenes: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to initialize scenes", e);
        }
    }

    /**
     * Initializes all game panes.
     */
    private  void initializeRooms() {
        panes.put("main_floor_view", new MainFloorView().createScene(this, controller));
        panes.put("combat_view", new CombatView().createScene(this, controller));
        panes.put("treasure_view", new TreasureView().createScene(this, controller));
        panes.put("stairs_view", new StairsView().createScene(this, controller));
        panes.put("change_weapon_view", new ChangeWeaponView().createScene(this, controller));
        panes.put("select_weapon_view", new SelectWeaponView().createScene(this, controller));
        panes.put("trap_view", new TrapView().createScene(this, controller));
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
        if ("difficulty_menu".equals(sceneName) && !isTowerLoaded()) {
            LOGGER.warn("No tower loaded. Please load a tower from the modding menu to proceed.");
            return; // Do not switch scenes automatically
        } else if ("difficulty_menu".equals(sceneName)) {
            controller.resetGame();
            initializeRooms();
        }

        Pane pane = panes.get(sceneName);

        //recreating Panes for resetting the view
        if ("combat_view".equals(sceneName)) {
            pane = new CombatView().createScene(this, controller);
        }
        if ("select_weapon_view".equals(sceneName)) {
            pane = new SelectWeaponView().createScene(this, controller);
        }
        if ("stairs_view".equals(sceneName)) {
            pane = new StairsView().createScene(this, controller);
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

        // Notify view if it implements SceneActivationListener
        if (pane.getUserData() instanceof SceneActivationListener sceneActivationListener) {
            sceneActivationListener.onSceneActivated();
        }
    }

    /**
     * Checks if a tower is loaded in the game data manager.
     *
     * @return true if a tower is loaded, false otherwise
     */
    private boolean isTowerLoaded() {
        return GameDataManagerImpl.getInstance().getTowerPath().isPresent();
    }

    /**
     * Gets a copy of the primary stage of the application with only necessary properties.
     * This prevents exposing the internal stage representation.
     * 
     * @return a new Stage with copied properties from the internal stage
     */
    public Stage getStage() {
        final Stage stageProxy = new Stage();
        stageProxy.setX(stage.getX());
        stageProxy.setY(stage.getY());
        stageProxy.setWidth(stage.getWidth());
        stageProxy.setHeight(stage.getHeight());
        stageProxy.setTitle(stage.getTitle());
        stageProxy.setFullScreen(stage.isFullScreen());
        stageProxy.setMaximized(stage.isMaximized());
        return stageProxy;
    }
}
