package it.unibo.templetower.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
* Utility class for instantiation gradle build problem.
*/
public final class DialogUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DialogUtil.class);
    private static final int PADDING = 10;
    private static final int VBOX = 50;

    private DialogUtil() {
        // Utility class, prevent instantiation
    }

    /**
     * Displays a popup dialog with a message and handles the closing action.
     * 
     * @param dialog The dialog to be displayed.
     * @param message The message to be shown in the dialog.
     * @param onClose The callback to be executed when the dialog is closed.
     */
    public static void showDialog(final Dialog<?> dialog, final String message, final Runnable onClose) {
        final Label loseLabel = new Label(message);
        loseLabel.getStyleClass().add("label");

        final Button btLeave = new Button("Close");
        btLeave.setOnAction(event -> {
            dialog.setResult(null);
            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        final HBox btContainer = new HBox(btLeave);
        btContainer.setAlignment(Pos.CENTER);

        final VBox layout = new VBox(VBOX, loseLabel, btContainer);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(PADDING));

        dialog.setOnCloseRequest(event -> {
            LOGGER.info("Popup closed with X");
            dialog.setResult(null);
            if (onClose != null) {
                onClose.run();
            }
        });

        dialog.showAndWait();
    }
}
