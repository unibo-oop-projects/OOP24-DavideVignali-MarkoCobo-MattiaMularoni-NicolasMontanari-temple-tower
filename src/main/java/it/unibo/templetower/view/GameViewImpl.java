package it.unibo.templetower.view;

import java.io.FileNotFoundException;

import it.unibo.templetower.controller.GameController;
import it.unibo.templetower.controller.GameControllerImpl;
import it.unibo.templetower.model.RoomBehavior;
import javafx.application.Application;
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
        gc.changeRoom(1);

        System.out.println("Actual player room: " + gc.getPlayerActualRoom());
        
        SceneManager manager = new SceneManager(primaryStage);
        manager.switchTo("home");
    }
}
