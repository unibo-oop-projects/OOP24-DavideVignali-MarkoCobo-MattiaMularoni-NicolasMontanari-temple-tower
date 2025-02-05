package it.unibo.templetower.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MainFloorView {
    private Pane dPane;
    private Circle outer;
    private Circle inner;

    private static final double HEIGHT = 1280;
    private static final double WIDTH = 720;

    private static final int nRooms = 8;

    public Scene createScene(SceneManager manager) {
        BorderPane root = new BorderPane();
        root.setId("circle-room-back");

        dPane = new Pane();
        root.setCenter(dPane);

        outer = createCircle("outer-circle-rooms", HEIGHT - 300, WIDTH - 320, 280);
        inner = createCircle("inner-circle-rooms", HEIGHT - 300, WIDTH - 320, 180);

        Scene scene = new Scene(root, HEIGHT, WIDTH);

        // Listener per adattare la scena al ridimensionamento
        scene.widthProperty().addListener((observer, oldWidth, newWidth) -> adaptScene(scene));
        scene.heightProperty().addListener((observer, oldHeight, newHeight) -> adaptScene(scene));

        dPane.getChildren().addAll(outer, inner);
        return scene;
    }

    private void createButtons(double sceneWidth, double sceneHeight) {
        ToggleButton left = new ToggleButton("<");
        ToggleButton right = new ToggleButton(">");
        ToggleButton pause = new ToggleButton("||");

        
        
        HBox buttons = new HBox(left, pause, right);
        buttons.setPrefWidth(50);
        left.setMinWidth(buttons.getPrefWidth());
        right.setMinWidth(buttons.getPrefWidth());
        pause.setMinWidth(buttons.getPrefWidth());

        buttons.getStyleClass().add("buttons");
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        buttons.setLayoutX(sceneWidth/2-((buttons.getPrefWidth()*3)/2));
        buttons.setLayoutY(sceneHeight/1.1);

        dPane.getChildren().add(buttons);
    }

    private Circle createCircle(String id, double centerX, double centerY, double radius) {
        Circle circle = new Circle(centerX, centerY, radius);
        circle.setId(id);
        return circle;
    }

    private void adaptScene(Scene scene) {
        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        double centerX = sceneWidth / 2;
        double centerY = sceneHeight / 2.5;

        updateCirclePositionAndRadius(outer, centerX, centerY, Math.min(sceneWidth, sceneHeight) / 3);
        updateCirclePositionAndRadius(inner, centerX, centerY, outer.getRadius() * 0.50);

        double roomRadius = (outer.getRadius() + inner.getRadius()) / 2;

        // Rimuove elementi esistenti (stanze, etichette, settori e linee)
        dPane.getChildren().removeIf(node -> node instanceof Rectangle || node instanceof Text || node instanceof Arc || node instanceof Line || node instanceof HBox);
        createButtons(sceneWidth, sceneHeight);
        // Crea nuovamente stanze, settori e linee
        for (int i = 0; i < nRooms; i++) {
            createRoomAndSector(i, centerX, centerY, roomRadius);
        }
    }

    private void updateCirclePositionAndRadius(Circle circle, double centerX, double centerY, double radius) {
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        circle.setRadius(radius);
    }

    private void createRoomAndSector(int roomIndex, double centerX, double centerY, double roomRadius) {
        double angle = 2 * Math.PI / nRooms * roomIndex;
        double x = centerX + roomRadius * Math.cos(angle) - 30; // Compensa per il centro della stanza
        double y = centerY + roomRadius * Math.sin(angle) - 30;

        // Aggiungi stanza
        Rectangle room = createRoom(x, y, roomIndex);
        dPane.getChildren().add(room);

        // Aggiungi etichetta
        Text roomLabel = createRoomLabel(x, y, roomIndex);
        dPane.getChildren().add(roomLabel);

        // Aggiungi settore
        Arc sector = createSector(centerX, centerY, outer.getRadius(), roomIndex);
        dPane.getChildren().add(sector);

        // Aggiungi linea divisoria
        Line line = createDivisionLine(centerX, centerY, angle);
        dPane.getChildren().add(line);
    }

    private Rectangle createRoom(double x, double y, int roomIndex) {
        Rectangle room = new Rectangle(x, y, 50, 50);
        room.getStyleClass().add("room");
        room.setFill(Color.LIGHTBLUE);
        room.setStroke(Color.BLACK);
        room.setOnMouseClicked(e -> System.out.println("Hai cliccato sulla stanza R" + (roomIndex + 1)));
        return room;
    }

    private Text createRoomLabel(double x, double y, int roomIndex) {
        Text label = new Text(x + 10, y + 25, "R" + (roomIndex + 1));
        label.setFill(Color.BLACK);
        return label;
    }

    private Arc createSector(double centerX, double centerY, double outerRadius, int roomIndex) {
        double startAngle = roomIndex * (360.0 / nRooms) + 200;
        double sectorLength = 360.0 / nRooms;

        Arc sector = new Arc(centerX, centerY, outerRadius, outerRadius, startAngle, sectorLength);
        sector.getStyleClass().add("sector");
        sector.setType(ArcType.ROUND);
        return sector;
    }

    private Line createDivisionLine(double centerX, double centerY, double angle) {
        angle = angle-90; // Compensa per il centro della stanza
        double startX = centerX + inner.getRadius() * Math.cos(angle + Math.PI / 2); // Cerchio interno
        double startY = centerY + inner.getRadius() * Math.sin(angle + Math.PI / 2);
        double endX = centerX + outer.getRadius() * Math.cos(angle + Math.PI / 2);   // Cerchio esterno
        double endY = centerY + outer.getRadius() * Math.sin(angle + Math.PI / 2);

        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.WHITE);
        return line;
    }

}