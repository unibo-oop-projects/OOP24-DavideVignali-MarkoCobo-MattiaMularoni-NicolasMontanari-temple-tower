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
public class ModdingMenuView implements SceneActivationListener {

    private static final String POPUP_MESSAGE = "Using the modding menu will create a folder in the user directory to save towers. If you want to delete it, use the clear button.";
    private static final double POPUP_WIDTH = 400;
    private static final double POPUP_HEIGHT = 200;

    private boolean hasShownPopup;
    private Scene scene;
    private Stage ownerStage;

    /**
     * Creates and returns the modding menu scene.
     *
     * @param manager The scene manager to handle scene transitions
     * @return A new Scene object containing the modding menu interface
     */
    public Scene createScene(final SceneManager manager) {
        this.ownerStage = manager.getStage();

        // Get primary screen dimensions
        final double screenWidth = Screen.getPrimary().getBounds().getWidth();
        final double screenHeight = Screen.getPrimary().getBounds().getHeight();

        // Set window dimensions to half the primary screen size
        final double windowWidth = screenWidth / 2;
        final double windowHeight = screenHeight / 2;

        // Create root container
        final StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        // Add background image
        final ImageView background = new ImageView(new Image("/images/modding_menu_bg.png"));
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
        this.scene = new Scene(root, windowWidth, windowHeight);
        this.scene.getStylesheets().add(getClass().getResource("/css/modding_menu.css").toExternalForm());
        
        // Set this view as the scene's user data for activation callbacks
        this.scene.setUserData(this);

        return this.scene;
    }

    @Override
    public void onSceneActivated() {
        if (!hasShownPopup) {
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
        final Stage popupStage = new Stage(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(ownerStage);

        final VBox popupRoot = new VBox(10);
        popupRoot.getStyleClass().add("popup-root");

        final Label message = new Label(POPUP_MESSAGE);
        message.getStyleClass().add("popup-label");
        message.setWrapText(true);
        message.setPrefWidth(POPUP_WIDTH - 40); // Account for padding

        final Button closeButton = new Button("Close");
        closeButton.getStyleClass().add("popup-button");
        closeButton.setOnAction(e -> popupStage.close());

        popupRoot.getChildren().addAll(message, closeButton);
        
        final Scene popupScene = new Scene(popupRoot, POPUP_WIDTH, POPUP_HEIGHT);
        popupScene.getStylesheets().add(getClass().getResource("/css/modding_menu.css").toExternalForm());
        
        popupStage.setScene(popupScene);

        // Center the popup relative to the current scene's window
        popupStage.setX(ownerStage.getX() + (scene.getWidth() - POPUP_WIDTH) / 2);
        popupStage.setY(ownerStage.getY() + (scene.getHeight() - POPUP_HEIGHT) / 2);
        
        popupStage.show();
    }
}
