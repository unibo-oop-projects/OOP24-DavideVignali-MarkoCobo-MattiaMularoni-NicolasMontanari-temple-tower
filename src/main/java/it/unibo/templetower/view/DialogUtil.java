package it.unibo.templetower.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* c.
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
            dialog.setResult(null); // Imposta un risultato per chiudere la dialog
            dialog.close();
            if (onClose != null) {
                onClose.run();
            }
        });

        // Contenitore per il bottone
        final HBox btContainer = new HBox(btLeave);
        btContainer.setAlignment(Pos.CENTER);

        // Contenitore principale con testo e bottone
        final VBox layout = new VBox(VBOX, loseLabel, btContainer);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(PADDING));

        // Imposta il contenuto della finestra
        dialog.getDialogPane().setContent(layout);

        // Permette la chiusura con la X
        dialog.setOnCloseRequest(_ -> {
            LOGGER.info("Popup closed with X");
            dialog.setResult(null);
            if (onClose != null) {
                onClose.run();
            }
        });

        // Mostra la finestra di dialogo
        dialog.showAndWait();
    }
}
