package it.unibo.templetower.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;

/**
 * Class responsible for creating and managing the modding menu scene.
 * This class provides the modding menu interface for the game.
 * Implements MVC pattern as part of the View layer.
 */
public final class ModdingMenuView implements SceneActivationListener {

    private static final String POPUP_MESSAGE = 
        "Using the modding menu will create a folder in the user directory to save towers. "
        + "If you want to delete it, use the clear button.";
    private static final double POPUP_WIDTH_RATIO = 0.4;
    private static final double POPUP_HEIGHT_RATIO = 0.2;
    private static final double PADDING_OFFSET = 40.0;
    private static final String CSS_RESOURCE_PATH = "/css/modding_menu.css";
    private static final String BACKGROUND_RESOURCE_PATH = "/images/modding_menu_bg.png";

    private boolean hasShownPopup;
    private Scene scene;
    private double stageX;
    private double stageY;
    private double stageWidth;
    private double stageHeight;

    /**
     * Creates and returns the modding menu scene.
     *
     * @param manager The scene manager to handle scene transitions
     * @return A new Scene object containing the modding menu interface
     */
    public Scene createScene(final SceneManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("SceneManager cannot be null");
        }

        // Store stage properties
        this.stageX = manager.getX();
        this.stageY = manager.getY();
        this.stageWidth = manager.getWidth();
        this.stageHeight = manager.getHeight();

        // Get primary screen dimensions
        final double screenWidth = Screen.getPrimary().getBounds().getWidth();
        final double screenHeight = Screen.getPrimary().getBounds().getHeight();

        // Set window dimensions to half the primary screen size
        final double windowWidth = screenWidth / 2;
        final double windowHeight = screenHeight / 2;

        // Create root container
        final StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        // Load background image safely
        final String backgroundUrl = getClass().getResource(BACKGROUND_RESOURCE_PATH).toExternalForm();
        if (backgroundUrl == null) {
            throw new IllegalStateException("Background image resource not found: " + BACKGROUND_RESOURCE_PATH);
        }
        final ImageView background = new ImageView(new Image(backgroundUrl));
        background.setPreserveRatio(true);
        background.setFitWidth(windowWidth);
        background.setFitHeight(windowHeight);

        // Make background responsive to window resizing
        root.widthProperty().addListener((obs, old, newVal) -> {
            double newWidth = newVal.doubleValue();
            double newHeight = newWidth * background.getImage().getHeight() / background.getImage().getWidth();
            if (newHeight < root.getHeight()) {
                newHeight = root.getHeight();
                newWidth = newHeight * background.getImage().getWidth() / background.getImage().getHeight();
            }
            background.setFitWidth(newWidth);
            background.setFitHeight(newHeight);
        });

        root.heightProperty().addListener((obs, old, newVal) -> {
            double newHeight = newVal.doubleValue();
            double newWidth = newHeight * background.getImage().getWidth() / background.getImage().getHeight();
            if (newWidth < root.getWidth()) {
                newWidth = root.getWidth();
                newHeight = newWidth * background.getImage().getHeight() / background.getImage().getWidth();
            }
            background.setFitWidth(newWidth);
            background.setFitHeight(newHeight);
        });

        // Add a placeholder label with styling
        final Label placeholder = new Label("Modding Menu");
        placeholder.getStyleClass().add("modding-menu-label");
        root.getChildren().addAll(background, placeholder);

        // Create the scene and store it
        final Scene newScene = new Scene(root, windowWidth, windowHeight);
        final String cssUrl = getClass().getResource(CSS_RESOURCE_PATH).toExternalForm();
        if (cssUrl == null) {
            throw new IllegalStateException("CSS resource not found: " + CSS_RESOURCE_PATH);
        }
        newScene.getStylesheets().add(cssUrl);

        // Set this view as the scene's user data for activation callbacks
        newScene.setUserData(this);
        this.scene = newScene;

        return new Scene(root, windowWidth, windowHeight);
    }

    /**
     * Called when the scene is activated.
     */
    @Override
    public void onSceneActivated() {
        if (!hasShownPopup && scene != null) {
            showFirstTimePopup();
            hasShownPopup = true;
        }
    }

    /**
     * Shows an informative popup when the modding menu is opened.
     * The popup is styled using CSS and properly sized to fit its content.
     * The popup is modal and centered on top of the modding menu.
     */
    private void showFirstTimePopup() {
        if (scene == null) {
            return;
        }

        final Stage popupStage = new Stage(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);

        final VBox popupRoot = new VBox(10);
        popupRoot.getStyleClass().add("popup-root");

        final Label message = new Label(POPUP_MESSAGE);
        message.getStyleClass().add("popup-label");
        message.setWrapText(true);
        message.setPrefWidth(stageWidth * POPUP_WIDTH_RATIO - PADDING_OFFSET);

        final Button closeButton = new Button("Close");
        closeButton.getStyleClass().add("popup-button");
        closeButton.setOnAction(e -> popupStage.close());

        popupRoot.getChildren().addAll(message, closeButton);

        final Scene popupScene = new Scene(
            popupRoot, 
            stageWidth * POPUP_WIDTH_RATIO, 
            stageHeight * POPUP_HEIGHT_RATIO
        );

        final String cssUrl = getClass().getResource(CSS_RESOURCE_PATH).toExternalForm();
        popupScene.getStylesheets().add(cssUrl);

        popupStage.setScene(popupScene);

        // Center the popup relative to the current scene's window
        popupStage.setX(stageX + (scene.getWidth() - stageWidth * POPUP_WIDTH_RATIO) / 2);
        popupStage.setY(stageY + (scene.getHeight() - stageHeight * POPUP_HEIGHT_RATIO) / 2);

        popupStage.show();
    }
}
