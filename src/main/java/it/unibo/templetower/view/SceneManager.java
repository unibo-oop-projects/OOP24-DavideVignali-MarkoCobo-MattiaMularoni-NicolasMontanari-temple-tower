package it.unibo.templetower.view;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();

    public SceneManager(Stage stage) {
        this.stage = stage;
        initializeScenes();
    }

    private void initializeScenes() {
        scenes.put("difficulty_menu", new DifficultyMenu().createScene(this));
        scenes.put("main_floor_view", new MainFloorView().createScene(this));
        scenes.put("combat_view", new CombatView().createScene(this));
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