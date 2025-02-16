package it.unibo.templetower.utils;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Utility class for handling background image operations.
 */
public final class BackgroundUtils {

    private BackgroundUtils() {
        // Utility class should not be instantiated
    }

    /**
     * Sets up the background image resizing listeners.
     *
     * @param root The root StackPane containing the background
     * @param background The background ImageView to be resized
     */
    public static void setupBackgroundResizing(final StackPane root, final ImageView background) {
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
}
