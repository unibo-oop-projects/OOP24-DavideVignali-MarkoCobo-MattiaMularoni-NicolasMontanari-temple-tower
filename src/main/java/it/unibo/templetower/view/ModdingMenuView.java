package it.unibo.templetower.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import it.unibo.templetower.controller.ModdingMenuController;
import it.unibo.templetower.utils.Pair;

/**
 * Class responsible for creating and managing the modding menu scene.
 * This class provides the modding menu interface for the game.
 * Implements MVC pattern as part of the View layer.
 */
public final class ModdingMenuView implements SceneActivationListener {

    private static final String POPUP_MESSAGE = "Using the modding menu will create a folder in the user directory to save towers"
            + " If you want to delete it, use the clear button.";
    private static final double POPUP_WIDTH = 400;
    private static final double POPUP_HEIGHT = 200;
    private static final double POPUP_PADDING = 40;
    private static final double SCREEN_DIVISION_FACTOR = 2.0;
    private static final double SPACING = 10;
    private static final double BUTTON_WIDTH = 200;
    private static final String MODDING_MENU_CSS = "/css/modding_menu.css";
    private static final String POPUP_BUTTON_STYLE = "popup-button";

    private boolean hasShownPopup;
    private Stage ownerStage;
    private ListView<HBox> towerList; // Changed from ListView<String> to ListView<HBox>
    private final List<String> importedTowers;
    private final ModdingMenuController controller;
    private String selectedTowerName; // Track currently selected tower

    /**
     * Creates a new ModdingMenuView with initialized fields.
     */
    public ModdingMenuView() {
        this.hasShownPopup = false;
        this.ownerStage = null;
        this.towerList = new ListView<>(); // Now initialized as ListView<HBox>
        this.importedTowers = new ArrayList<>();
        this.controller = new ModdingMenuController();
        this.selectedTowerName = null;
    }

    /**
     * Creates and returns the modding menu scene.
     *
     * @param manager The scene manager to handle scene transitions
     * @return A new Scene object containing the modding menu interface
     */
    public Scene createScene(final SceneManager manager) {
        this.ownerStage = Objects.requireNonNull(manager.getStage(), "Stage cannot be null");

        final Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        final double windowWidth = primaryScreenBounds.getWidth() / SCREEN_DIVISION_FACTOR;
        final double windowHeight = primaryScreenBounds.getHeight() / SCREEN_DIVISION_FACTOR;

        final StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        final ImageView background = new ImageView(getClass().getResource("/Images/modding_menu_bg.png").toExternalForm());
        background.setPreserveRatio(true);
        background.setFitWidth(windowWidth);
        background.setFitHeight(windowHeight);

        setupBackgroundResizing(root, background);

        // Create main content container
        final VBox content = new VBox(SPACING);
        content.setAlignment(Pos.CENTER);
        content.getStyleClass().add("modding-content");

        // Title
        final Label title = new Label("Modding Menu");
        title.getStyleClass().add("modding-menu-label");

        // Create tower list
        towerList = new ListView<>();
        towerList.getStyleClass().add("tower-list");
        towerList.setPrefHeight(windowHeight * 0.5);
        VBox.setVgrow(towerList, Priority.ALWAYS);

        // Create buttons
        final Button importFolderButton = createStyledButton("Import Folder", this::handleImportFolder);
        final Button importZipButton = createStyledButton("Import ZIP", this::handleImportZip);
        final Button clearButton = createStyledButton("Clear", this::handleClearAction);
        final Button exitButton = createStyledButton("Exit", () -> manager.switchTo("enter_menu"));

        // Buttons container
        final HBox buttonContainer = new HBox(SPACING);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(importFolderButton, importZipButton, clearButton);

        // Add all components to content
        content.getChildren().addAll(title, towerList, buttonContainer, exitButton);

        root.getChildren().addAll(background, content);

        final Scene newScene = new Scene(root, windowWidth, windowHeight);
        newScene.getStylesheets().add(getClass().getResource(MODDING_MENU_CSS).toExternalForm());
        newScene.setUserData(this);

        ownerStage.setWidth(windowWidth);
        ownerStage.setHeight(windowHeight);
        ownerStage.centerOnScreen();

        return newScene;
    }

    private Button createStyledButton(final String text, final Runnable action) {
        final Button button = new Button(text);
        button.getStyleClass().add("modding-button");
        button.setPrefWidth(BUTTON_WIDTH);
        button.setOnAction(e -> action.run());
        return button;
    }

    private void handleImportFolder() {
        final DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select Tower Folder");
        dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        final File selectedDirectory = dirChooser.showDialog(ownerStage);
        if (selectedDirectory != null) {
            final Optional<String> error = controller.importFolder(selectedDirectory);
            if (error.isPresent()) {
                showErrorDialog("Import Error", error.get());
            } else {
                importedTowers.clear();
                importedTowers.addAll(controller.getImportedTowers());
                updateTowerList();
                showSuccessDialog("Import Successful", "Tower has been imported successfully!");
            }
        }
    }

    private void handleImportZip() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Tower ZIP");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip")
        );
        final File file = fileChooser.showOpenDialog(ownerStage);
        if (file != null) {
            final Optional<String> error = controller.importZip(file);
            if (error.isPresent()) {
                showErrorDialog("Import Error", error.get());
            } else {
                importedTowers.clear();
                importedTowers.addAll(controller.getImportedTowers());
                updateTowerList();
                showSuccessDialog("Import Successful", "Tower has been imported successfully!");
            }
        }
    }

    private void updateTowerList() {
        towerList.getItems().clear();
        for (final String towerDirName : importedTowers) {
            final Optional<Pair<String, String>> towerInfo = controller.getTowerInfo(towerDirName);
            final HBox itemContainer = new HBox(10);
            itemContainer.setAlignment(Pos.CENTER_LEFT);
            final Label towerLabel = new Label();
            HBox.setHgrow(towerLabel, Priority.ALWAYS);
            towerLabel.setMaxWidth(Double.MAX_VALUE);
            if (towerInfo.isPresent()) {
                final Pair<String, String> info = towerInfo.get();
                try {
                    final int height = controller.getTowerHeight(towerDirName);
                    towerLabel.setText(info.getX() + " (Height: " + height + ") - " + info.getY());
                } catch (IllegalArgumentException ex) {
                    towerLabel.setText(towerDirName + " (Invalid tower: " + ex.getMessage() + ")");
                }
            } else {
                towerLabel.setText(towerDirName + " (Invalid tower)");
            }
            final HBox buttonsContainer = new HBox(5);
            buttonsContainer.setAlignment(Pos.CENTER_RIGHT);
            final Button selectButton = new Button(towerDirName.equals(selectedTowerName) ? "Selected" : "Select");
            selectButton.getStyleClass().addAll("select-button",
                towerDirName.equals(selectedTowerName) ? "selected-button" : "");
            selectButton.setOnAction(e -> handleSelectTower(towerDirName));
            final Button deleteButton = new Button("X");
            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setOnAction(e -> handleDeleteTower(towerDirName));
            buttonsContainer.getChildren().addAll(selectButton, deleteButton);
            itemContainer.getChildren().addAll(towerLabel, buttonsContainer);
            if (towerDirName.equals(selectedTowerName)) {
                itemContainer.getStyleClass().add("selected-tower-item");
            }
            towerList.getItems().add(itemContainer);
        }
    }

    /**
     * Handles the selection of a tower for gameplay.
     * Shows confirmation dialog and updates the game data manager with the selected tower.
     *
     * @param towerName the name of the tower to select
     */
    private void handleSelectTower(final String towerName) {
        final Optional<String> error = controller.selectTower(towerName);
        if (error.isPresent()) {
            showErrorDialog("Tower Selection Error", error.get());
        } else {
            selectedTowerName = towerName; // Update selected tower
            showSuccessDialog("Tower Selected", "Tower '" + towerName + "' has been selected for gameplay.");
            updateTowerList(); // Refresh list to show selection
        }
    }

    /**
     * Handles the deletion of a specific tower.
     * Shows a confirmation dialog before deleting.
     *
     * @param towerName the name of the tower to delete
     */
    private void handleDeleteTower(final String towerName) {
        final Button yesButton = new Button("Yes");
        yesButton.getStyleClass().add(POPUP_BUTTON_STYLE);

        final Button noButton = new Button("No");
        noButton.getStyleClass().add(POPUP_BUTTON_STYLE);

        final Stage popupStage = createConfirmationPopup(
            "Are you sure you want to delete the tower: " + towerName + "?",
            yesButton,
            noButton
        );

        yesButton.setOnAction(e -> {
            final Optional<String> error = controller.deleteTower(towerName);
            if (error.isPresent()) {
                showErrorDialog("Delete Error", error.get());
            } else {
                if (towerName.equals(selectedTowerName)) {
                    selectedTowerName = null;
                    controller.clearSelectedTower();
                }
                showSuccessDialog("Delete Successful", "Tower deleted successfully.");
                importedTowers.clear();
                importedTowers.addAll(controller.getImportedTowers());
                updateTowerList();
            }
            popupStage.close();
        });

        noButton.setOnAction(e -> popupStage.close());
        popupStage.showAndWait();
    }

    /**
     * Handles the clear action by showing a confirmation popup.
     * In the MVC pattern, the view manages the popup while the controller will later be called to perform deletion.
     * Currently, both Yes and No buttons close the popup.
     */
    private void handleClearAction() {
        final Button yesButton = new Button("Yes");
        yesButton.getStyleClass().add(POPUP_BUTTON_STYLE);

        final Button noButton = new Button("No");
        noButton.getStyleClass().add(POPUP_BUTTON_STYLE);

        final Stage popupStage = createConfirmationPopup(
            "Are you sure you want to delete the entire folder?",
            yesButton,
            noButton
        );

        yesButton.setOnAction(e -> {
            final Optional<String> error = this.controller.clearTowersDirectory();
            if (error.isPresent()) {
                showErrorDialog("Clear Error", error.get());
            } else {
                selectedTowerName = null;
                controller.clearSelectedTower();
                showSuccessDialog("Clear Successful", "All towers removed successfully.");
                this.importedTowers.clear();
                this.importedTowers.addAll(this.controller.getImportedTowers());
                updateTowerList();
            }
            popupStage.close();
        });

        noButton.setOnAction(e -> popupStage.close());
        popupStage.showAndWait();
    }

    private void showErrorDialog(final String title, final String content) {
        showStyledDialog(Alert.AlertType.ERROR, title, content);
    }

    private void showSuccessDialog(final String title, final String content) {
        showStyledDialog(Alert.AlertType.INFORMATION, title, content);
    }

    private void showStyledDialog(final Alert.AlertType type, final String title, final String content) {
        final Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().getStylesheets().add(getClass().getResource(MODDING_MENU_CSS).toExternalForm());
        alert.getDialogPane().getStyleClass().add("alert-dialog");
        alert.showAndWait();
    }

    /**
     * Sets up the background image resizing listeners.
     *
     * @param root The root StackPane containing the background
     * @param background The background ImageView to be resized
     */
    private void setupBackgroundResizing(final StackPane root, final ImageView background) {
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
    }

    /**
     * Called when this scene becomes active.
     * Shows a first-time popup if it hasn't been shown before.
     */
    @Override
    public void onSceneActivated() {
        if (!hasShownPopup) {
            showFirstTimePopup();
            hasShownPopup = true;
        }
        // Refresh tower list when scene is activated
        importedTowers.clear();
        importedTowers.addAll(controller.getImportedTowers());
        updateTowerList();
    }

    /**
     * Shows an informative popup when the modding menu is opened.
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
        message.setPrefWidth(POPUP_WIDTH - POPUP_PADDING);
        final Button closeButton = new Button("Close");
        closeButton.getStyleClass().add(POPUP_BUTTON_STYLE);
        closeButton.setOnAction(e -> popupStage.close());
        popupRoot.getChildren().addAll(message, closeButton);
        final Scene popupScene = new Scene(popupRoot, POPUP_WIDTH, POPUP_HEIGHT);
        popupScene.getStylesheets().add(getClass().getResource(MODDING_MENU_CSS).toExternalForm());
        popupStage.setScene(popupScene);
        popupStage.centerOnScreen();
        popupStage.show();
    }

    // Add this helper method for creating popups
    private Stage createConfirmationPopup(final String message, final Button... buttons) {
        final Stage popupStage = new Stage(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(ownerStage);

        final VBox popupRoot = new VBox(10);
        popupRoot.getStyleClass().add("popup-root");

        final Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("popup-label");
        messageLabel.setWrapText(true);
        messageLabel.setPrefWidth(POPUP_WIDTH - POPUP_PADDING);

        final HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(buttons);

        popupRoot.getChildren().addAll(messageLabel, buttonContainer);

        final Scene popupScene = new Scene(popupRoot, POPUP_WIDTH, POPUP_HEIGHT);
        popupScene.getStylesheets().add(getClass().getResource(MODDING_MENU_CSS).toExternalForm());
        popupStage.setScene(popupScene);
        popupStage.centerOnScreen();

        return popupStage;
    }
}
