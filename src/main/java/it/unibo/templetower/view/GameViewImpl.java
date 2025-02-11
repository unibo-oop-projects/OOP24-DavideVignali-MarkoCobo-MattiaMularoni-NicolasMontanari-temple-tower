package it.unibo.templetower.view;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameViewImpl extends Application {
    @Override
    public void start(final Stage primaryStage) throws FileNotFoundException {
        SceneManager manager = new SceneManager(primaryStage);
        manager.switchTo("home");
    }
}
