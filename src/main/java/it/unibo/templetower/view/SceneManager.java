package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import it.unibo.templetower.controller.GameController;
import it.unibo.templetower.controller.GameControllerImpl;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();
    private final GameController controller;

    public SceneManager(Stage stage) {
        this.stage = stage;
        this.controller = new GameControllerImpl();
        initializeScenes();
    }

    private void initializeScenes()  {
        scenes.put("difficulty_menu", new DifficultyMenu().createScene(this, controller));
        scenes.put("main_floor_view", new MainFloorView().createScene(this, controller));
        scenes.put("combat_view", new CombatView().createScene(this, controller));
        scenes.put("treasure_view", new TreasureView().createScene(this, controller));
        scenes.put("stairs_view", new StairsView().createScene(this, controller));
        try {
            scenes.put("enter_menu", new EnterMenu().createScene(this));
        } catch (FileNotFoundException ex) {
        }
        try{
            scenes.put("home", new Home().createScene(this));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void switchTo(String sceneName) {
        Scene scene = scenes.get(sceneName);
        String css = this.getClass().getResource("/css/SimpleGui.css").toExternalForm(); 
        if (scene != null) {
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();
        } else {
            throw new IllegalArgumentException("Scene " + sceneName + " not found");
        }
    }
}