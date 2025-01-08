package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.io.InputStream;

import it.unibo.templetower.controller.GameController;
import it.unibo.templetower.controller.GameControllerImpl;
import it.unibo.templetower.model.RoomBehavior;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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

        /* Test player e cambio stanza*/
        GameController gc = new GameControllerImpl();
        gc.changeRoom(1);
        gc.changeRoom(1);

        System.out.println("Actual player room: " + gc.getPlayerActualRoom());

        // Wrappare ImageView in un contenitore StackPane
        HBox hbox = new HBox();

        // set spacing 
        hbox.setSpacing(10);

        // set alignment for the HBox 
        hbox.setAlignment(Pos.CENTER);

        // create a scene 
        Scene scene = new Scene(hbox, 280, 280);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/INITemp.png");
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: images/INITemp.png");
        }

        // create a image 
        Image image = new Image(inputStream);

        // create a background image 
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        // create Background 
        Background background = new Background(backgroundimage);

        // set background 
        hbox.setBackground(background);

        // Imposta la scena
        primaryStage.setScene(scene); // Assegna la scena al primo stage
        primaryStage.setTitle("Primo Stage");
        primaryStage.show();

        Stage secondaryStage = new Stage();

        // Passaggio al secondo stage al clic di un qualsiasi tasto
        scene.setOnKeyPressed(event -> {
            configureSecondaryStage(secondaryStage);
            secondaryStage.show();
        });
    }

    public void configureSecondaryStage(Stage stage) {
        // Configura il layout della seconda schermata
        Label message = new Label("Benvenuto nella seconda schermata!");
        Button closeButton = new Button("Chiudi");
        closeButton.setOnAction(event -> stage.close());

        StackPane secondaryRoot = new StackPane(message, closeButton);
        StackPane.setAlignment(closeButton, javafx.geometry.Pos.BOTTOM_CENTER);
        Scene secondaryScene = new Scene(secondaryRoot, 600, 400);

        // Configura le proprietà del secondo stage
        stage.setScene(secondaryScene);
        stage.setTitle("Secondo Stage");
        stage.setResizable(false); // Impedisce il ridimensionamento
    }
}
