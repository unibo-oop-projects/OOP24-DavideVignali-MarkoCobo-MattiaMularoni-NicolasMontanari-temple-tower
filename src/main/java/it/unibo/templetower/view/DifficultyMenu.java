package it.unibo.templetower.view;

import java.io.InputStream;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DifficultyMenu {

    public Scene createScene(SceneManager manager) {
        // Creazione dello StackPane per gestire sfondo e contenuti
        StackPane root = new StackPane();

        // Carica l'immagine di sfondo
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/menu.png");
       

        // Creazione dell'immagine di sfondo
        Image backgroundImage = new Image(inputStream);

        // Configura l'ImageView per lo sfondo
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false); // Riempire lo spazio disponibile
        backgroundView.setFitWidth(800); // Dimensioni iniziali
        backgroundView.setFitHeight(600);

        // Adatta l'immagine al ridimensionamento della finestra
        root.widthProperty().addListener((obs, oldVal, newVal) -> backgroundView.setFitWidth(newVal.doubleValue()));
        root.heightProperty().addListener((obs, oldVal, newVal) -> backgroundView.setFitHeight(newVal.doubleValue()));

        // Crea un layout verticale per i pulsanti
        VBox buttonLayout = new VBox(10);
        buttonLayout.setAlignment(Pos.CENTER);

        // Pulsante FACILE
        Button easyButton = new Button("FACILE");
        easyButton.setOnAction(e -> manager.switchTo("main_floor_view"));

        // Pulsante INTERMEDIO
        Button mediumButton = new Button("INTERMEDIO");
        mediumButton.setOnAction(e -> manager.switchTo("main_floor_view"));

        // Pulsante DIFFICILE
        Button hardButton = new Button("DIFFICILE");
        hardButton.setOnAction(e -> manager.switchTo("main_floor_view"));

        // Aggiungi i pulsanti al layout
        buttonLayout.getChildren().addAll(easyButton, mediumButton, hardButton);

        // Aggiungi lo sfondo e il layout pulsanti al root
        root.getChildren().addAll(backgroundView, buttonLayout);

        // Crea e ritorna la scena
        return new Scene(root, 800, 600);
    }
}
