package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.io.InputStream;

import it.unibo.templetower.model.RoomBehavior;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameViewImpl extends Application implements GameView {

    @Override
    public void displayRoom(RoomBehavior room) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayRoom'");
    }

    @Override
    public void updateStatusBar(int health, int experience) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateStatusBar'");
    }

    @Override
    public void showGameOver() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showGameOver'");
    }

    @Override
    public void showVictory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showVictory'");
    }

    @Override
    public void start(final Stage primaryStage) throws FileNotFoundException {

        StackPane stackPane = new StackPane();

        // create a scene 
        Scene scene = new Scene(stackPane, 800, 800);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/SchermataIniziale.png");
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: images/SchermataIniziale.png");
        }

        // create a image 
        Image image = new Image(inputStream);

        // create a background image 
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        // create Background 
        Background background = new Background(backgroundimage);

        // set background 
        stackPane.setBackground(background);

        // Imposta la scena
        primaryStage.setScene(scene); // Assegna la scena al primo stage
        primaryStage.setTitle("Primo Stage");
        primaryStage.show();

        Stage secondaryStage = new Stage();

        // Passaggio al secondo stage al clic di un qualsiasi tasto
        scene.setOnKeyPressed(event -> {
            configureSecondaryStage(secondaryStage);
            secondaryStage.show();
            primaryStage.close();
        });

    }

    public void configureSecondaryStage(Stage stage) {

        // Configura il layout della seconda schermata
        Label message = new Label("Scegli la difficoltà di gioco!");
        Button BottoneFacile = new Button("FACILE");
        Button BottoneIntermedio = new Button("INTERMEDIO");
        Button BottoneDifficile = new Button("DIFFICILE");

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/menu.png");
        if (inputStream == null) {
            System.out.println("Immagine non trovata! Controlla il percorso.");
            return;
        }

        // create a image 
        Image image = new Image(inputStream);

        // create a background image 
        BackgroundImage backgroundimage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        StackPane secondaryRoot = new StackPane(message);
        secondaryRoot.setBackground(new Background(backgroundimage));

        VBox ButtonBox = new VBox(10, BottoneFacile, BottoneIntermedio, BottoneDifficile);
        ButtonBox.setAlignment(javafx.geometry.Pos.BOTTOM_CENTER);

        VBox layout = new VBox(20, message, ButtonBox);
        layout.setAlignment(javafx.geometry.Pos.BOTTOM_CENTER);
        secondaryRoot.getChildren().add(layout);
        Scene secondaryScene = new Scene(secondaryRoot, 600, 400);

        // Configura le proprietà del secondo stage
        stage.setScene(secondaryScene);
        stage.setTitle("Secondo Stage");
        stage.setResizable(false); // Impedisce il ridimensionamento
    }
}
