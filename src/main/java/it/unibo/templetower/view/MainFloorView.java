package it.unibo.templetower.view;

import java.util.HashMap;
import java.util.Map;

import it.unibo.templetower.controller.GameController;
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
import javafx.scene.text.Text;

public class MainFloorView {
    private Pane dPane;
    private Circle outer;
    private Circle inner;

    ToggleButton left;
    ToggleButton right;
    ToggleButton pause;
    HBox buttons;

    private static final double HEIGHT = 1280;
    private static final double WIDTH = 720;

    private static final double OUTER_RADIUS = 280;
    private static final double INNER_RADIUS = OUTER_RADIUS*0.5;
    
    private int nRooms;

    private final Map<Integer, Arc> sectorMap = new HashMap<>();

    public Scene createScene(SceneManager manager, GameController controller) {
        BorderPane root = new BorderPane();
        dPane = new Pane();
        root.setCenter(dPane);
        root.setId("circle-room-back");
        
        this.nRooms = controller.getRooms().size();
        outer = createCircle("outer-circle-rooms", OUTER_RADIUS);
        inner = createCircle("inner-circle-rooms", INNER_RADIUS);
        
        dPane.getChildren().addAll(outer, inner);
        Scene scene = new Scene(root, HEIGHT, WIDTH);
        
        scene.widthProperty().addListener((obs, oldVal, newVal) -> adaptScene(scene, controller));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> adaptScene(scene, controller));
        
        createButtons(controller);
        adaptScene(scene, controller);
        return scene;
    }

    private void createButtons(GameController controller) {
        left = new ToggleButton("<");
        right = new ToggleButton(">");
        pause = new ToggleButton("ENTRA");
        
        buttons = new HBox(left, pause, right);
        buttons.getStyleClass().add("buttons");
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        buttons.setPrefWidth(50);
        buttons.setPrefWidth(50);

        left.setMinWidth(buttons.getPrefWidth());
        right.setMinWidth(buttons.getPrefWidth());
        pause.setMinWidth(buttons.getPrefWidth());
        
        left.setOnMouseClicked(e -> handleRoomChange(controller, -1));
        right.setOnMouseClicked(e -> handleRoomChange(controller, 1));
        pause.setOnMouseClicked(e -> handleFloorEnter(controller));
        
        dPane.getChildren().add(buttons);
    }

    private void handleRoomChange(GameController controller, int direction) {
        controller.changeRoom(direction);
        highlightSector(controller.getPlayerActualRoom());
    }

    private void handleFloorEnter(GameController controller) {
        controller.enterFirstRoom();
        highlightSector(controller.getPlayerActualRoom());
    }

    private Circle createCircle(String id, double radius) {
        Circle circle = new Circle(radius);
        circle.setId(id);
        return circle;
    }

    private void adaptScene(Scene scene, GameController controller) {
        double centerX = scene.getWidth() / 2;
        double centerY = scene.getHeight() / 2.5;

        updateCirclePositionAndRadius(outer, centerX, centerY, Math.min(scene.getWidth(), scene.getHeight()) / 3);
        updateCirclePositionAndRadius(inner, centerX, centerY, Math.min(scene.getWidth(), scene.getHeight()) / 5);

        double roomRadius = (outer.getRadius() + inner.getRadius()) / 2;
        dPane.getChildren().removeIf(node -> node instanceof Arc || node instanceof Text || node instanceof Line || node instanceof HBox);

        buttons.setLayoutX(centerX - ((buttons.getPrefWidth() * 3) / 2));
        buttons.setLayoutY(scene.getHeight() / 1.1);
        

        dPane.getChildren().add(buttons);
        sectorMap.clear();
        controller.getRooms().forEach(room -> {
            createRoomAndSector(nRooms - room.getId() - 1, centerX, centerY, roomRadius);
        });
        inner.toFront();
    }

    private void updateCirclePositionAndRadius(Circle circle, double centerX, double centerY, double radius) {
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        circle.setRadius(radius);
    }

    private void createRoomAndSector(int roomIndex, double centerX, double centerY, double roomRadius) {
        double angle = 2 * Math.PI / nRooms * roomIndex;
        double x = centerX + roomRadius * Math.cos(angle) - 35; // Compensa per il centro della stanza
        double y = centerY + roomRadius * Math.sin(angle) - 35;

        // Aggiungi etichetta
        dPane.getChildren().add(createRoomLabel(x, y, roomIndex));

        // Aggiungi settore
        Arc sector = createSector(centerX, centerY, outer.getRadius(), roomIndex);
        sectorMap.put(roomIndex, sector);
        dPane.getChildren().add(sector);

        // Aggiungi linea divisoria
        dPane.getChildren().add(createDivisionLine(centerX, centerY, angle));
    }

    private void highlightSector(int roomIndex) {
        // Reset colori per tutti i settori
        sectorMap.values().forEach(sector -> sector.setFill(null));
        
        System.err.println("Highlighting room: " + roomIndex);
        // Cambia il colore solo del settore selezionato
        
        Arc selectedSector = sectorMap.get(roomIndex);
        if (selectedSector != null) {
            selectedSector.setFill(Color.YELLOW);
        }
    }

    private Text createRoomLabel(double x, double y, int roomIndex) {
        Text label = new Text(x + 10, y + 25, "R" + (roomIndex +1));
        label.setFill(Color.WHITE);
        return label;
    }

    private Arc createSector(double centerX, double centerY, double outerRadius, int roomIndex) {
        double startAngle = roomIndex * (360.0 / nRooms);
        startAngle = startAngle + 26.5; // Compensa per il centro della stanza
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