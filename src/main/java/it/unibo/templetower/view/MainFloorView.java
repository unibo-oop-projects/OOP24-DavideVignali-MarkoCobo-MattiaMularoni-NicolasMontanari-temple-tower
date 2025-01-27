package it.unibo.templetower.view;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MainFloorView {
    private Pane dPane;
    private Circle outer;
    private Circle inner;
    private static final double HEIGHT = 1280;
    private static final double WIDTH = 720;
    
    public Scene createScene(SceneManager manager) {
        BorderPane root = new BorderPane();
        root.setId("circle-room-back");
        dPane = new Pane();
        root.setCenter(dPane);

        outer = new Circle(HEIGHT-320, WIDTH-350, 300);
        inner = new Circle(HEIGHT-320, WIDTH-350, 200);
        outer.setId("circle-rooms");
        inner.setId("circle-rooms");

        for (int i = 0; i < 8; i++) {
            double angle = 2 * Math.PI / 8 * i;
            double x = HEIGHT-320 + 250 * Math.cos(angle) - 35; // Compensa per il centro della stanza
            double y = WIDTH-350 + 250 * Math.sin(angle) - 35;

            // Crea una stanza
            Rectangle room = new Rectangle(x, y, 65, 65);
            room.getStyleClass().add("room");

            room.setFill(Color.LIGHTBLUE);
            room.setStroke(Color.BLACK);

            // Aggiungi un'etichetta alla stanza
            Text roomLabel = new Text(x + 10, y + 25, "R" + (i + 1));
            roomLabel.setFill(Color.BLACK);

            // Aggiungi eventi interattivi alla stanza
            room.setOnMouseClicked(e -> System.out.println("Hai cliccato sulla stanza " + roomLabel.getText()));

            root.getChildren().addAll(room, roomLabel);
        }

        dPane.getChildren().add(outer);
        dPane.getChildren().add(inner);
        
        return new Scene(root, HEIGHT, WIDTH);
    }
}