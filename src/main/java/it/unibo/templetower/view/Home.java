package it.unibo.templetower.view;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;

public class Home {
    public Scene createScene(SceneManager manager) throws FileNotFoundException {
         // Wrappare ImageView in un contenitore StackPane
        HBox hbox = new HBox();

        // set spacing 
        hbox.setSpacing(10);

        // set alignment for the HBox 
        hbox.setAlignment(Pos.CENTER);

        // create a scene
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

        hbox.setOnKeyPressed(event -> {
            System.out.println("Key pressed");
            manager.switchTo("main_floor_view");
        });

        Button settingsButton = new Button("Go main floor");
        settingsButton.setOnAction(e -> manager.switchTo("main_floor_view"));
        

        // set background 
        hbox.setBackground(background);
        hbox.getChildren().addAll(settingsButton);

        return new Scene(hbox, 400, 300);
    }
}