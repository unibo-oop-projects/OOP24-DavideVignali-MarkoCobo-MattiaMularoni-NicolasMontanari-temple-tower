package it.unibo.templetower.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.templetower.controller.GameController;
import it.unibo.templetower.controller.GameControllerImpl;
import it.unibo.templetower.controller.GameDataManager;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private static final double INITIAL_HEIGHT = Screen.getPrimary().getBounds().getHeight() / 2;
    private static final double INITIAL_WIDTH = Screen.getPrimary().getBounds().getWidth() / 2;
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
        stage.setTitle("Temple Tower");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        initializeMenu();
    }

    private void initializeMenu() {
        try {
            panes.put("difficulty_menu", new DifficultyMenu().createScene(this, controller));
            panes.put("enter_menu", new EnterMenu().createScene(this));
            panes.put("settings_menu", new SettingsMenuView().createScene(this));
            panes.put("home", new StartupView().createScene(this));
            panes.put("modding_menu", new ModdingMenuView().createScene(this));
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
            showWarningDialog("No Tower Loaded", "Please load a tower from the modding menu before starting the game.");
            return;
        } else if ("difficulty_menu".equals(sceneName)) {
            controller.resetGame();
            initializeRooms();
        }

        Pane pane = panes.get(sceneName);

        //recreating Panes for resetting the view
        switch (sceneName) {
            case "combat_view" -> pane = new CombatView().createScene(this, controller);
            case "select_weapon_view" -> pane = new SelectWeaponView().createScene(this, controller);
            case "stairs_view" -> pane = new StairsView().createScene(this, controller);
            case "treasure_view" -> pane = new TreasureView().createScene(this, controller);
            case "trap_view" -> pane = new TrapView().createScene(this, controller);
            default -> {
                LOGGER.info("Cache");
            }
        }
        if (pane == null) {
            LOGGER.info("Scene " + sceneName + " not found");
            return;
        }
        applyStylesheet(pane);
        updateStage(pane);
    }

    /**
     * Shows a warning dialog with the specified message.
     *
     * @param title the title of the warning dialog
     * @param message the message to display in the dialog
     */
    private void showWarningDialog(final String title, final String message) {
        final Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        return GameDataManager.getInstance().getTowerPath().isPresent();
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


    /**
     * @param path of the image
     * @return background image from path
     */
    public ImageView getImage(final String path) {
        final Image backgroundImage;
        final File file = new File(path);
        backgroundImage = new Image(file.toURI().toString());
        final ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
        return backgroundView;
    }

    /**
     * Loads an image and binds it to the given StackPane.
     * 
     * @param manager    the SceneManager responsible for managing images.
     * @param root       the StackPane where the background image will be set.
     * @param imagePath  the path of the image.
     */
    public void setBackground(final SceneManager manager, final StackPane root, final String imagePath) {
        try {
            final ImageView backgroundView = manager.getImage(imagePath);
            backgroundView.setPreserveRatio(false);
            backgroundView.fitWidthProperty().bind(root.widthProperty());
            backgroundView.fitHeightProperty().bind(root.heightProperty());
            root.getChildren().add(backgroundView);
        } catch (IllegalArgumentException e) {
            final Label errorLabel = new Label("Background image not found.");
            errorLabel.getStyleClass().add("label");
            root.getChildren().add(errorLabel);
        }
    }
}
