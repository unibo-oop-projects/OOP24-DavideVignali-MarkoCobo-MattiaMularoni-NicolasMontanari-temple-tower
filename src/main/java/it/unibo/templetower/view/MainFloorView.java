package it.unibo.templetower.view;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.templetower.controller.GameController;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 * This scene represents a floor of the game, where the player can move between
 * rooms.
 * This class is designed for extension and provides the base implementation for
 * floor views.
 * Subclasses should override createScene to customize the floor appearance.
 */
public class MainFloorView {
    private static final double OUTER_RADIUS = Screen.getPrimary().getVisualBounds().getHeight() / 3;
    private static final double INNER_RADIUS = OUTER_RADIUS * 0.5;
    private static final double INNER_CIRCLE_RATIO = 5.0;
    private static final double BUTTON_VERTICAL_POSITION = 1.1;
    private static final double FADE_DURATION = 0.8;
    private static final double FADE_MIN_OPACITY = 0.3;
    private static final double ANGLE_OFFSET = 26.5;
    private static final double ROOM_LABEL_OFFSET = 25.0;
    private static final int SHADOW_SPREAD = 25;
    private static final int SHADOW_Y_OFFSET = 2;
    private static final Color HIGHLIGHT_COLOR = Color.rgb(138, 74, 243);
    private static final int SECTOR_ANGLE_OFFSET = 35;
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFloorView.class);
    private Pane dPane;
    private Circle outer;
    private Circle inner;
    private HBox buttons;
    private int nRooms;
    private final Map<Integer, Arc> sectorMap = new HashMap<>();

    /**
     * Creates and returns the main scene for the floor view.
     * 
     * @param manager    The scene manager
     * @param controller The game controller
     * @return The created scene
     */
    public BorderPane createScene(final SceneManager manager, final GameController controller) {
        // Background
        final BorderPane root = new BorderPane();
        dPane = new Pane();
        root.setCenter(dPane);
        root.setId("circle-room-back");

        // Inner and outer circles for create the rooms container
        this.nRooms = controller.getNumberOfRooms();
        outer = createCircle("outer-circle-rooms", OUTER_RADIUS);
        inner = createCircle("inner-circle-rooms", INNER_RADIUS);
        dPane.getChildren().addAll(outer, inner);

        // Listener for keeping the pane responsive to screen size changes
        root.widthProperty().addListener((obs, oldVal, newVal) -> adaptScene(root, controller));
        root.heightProperty().addListener((obs, oldVal, newVal) -> adaptScene(root, controller));

        // Control buttons
        createButtons(controller, manager);

        root.sceneProperty().addListener(
                (obs, oldVal, newVal) -> Platform.runLater(() -> highlightSector(controller.getPlayerActualRoom())));

        return root;
    }

    private void createButtons(final GameController controller, final SceneManager manager) {
        final ToggleButton left = new ToggleButton("<");
        final ToggleButton right = new ToggleButton(">");
        final ToggleButton enter = new ToggleButton("ENTRA");
        buttons = new HBox(left, enter, right);
        buttons.getStyleClass().add("buttons");
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        buttons.setPrefWidth(100);

        left.setMinWidth(buttons.getPrefWidth());
        right.setMinWidth(buttons.getPrefWidth());
        enter.setMinWidth(buttons.getPrefWidth());

        // When the left button is clicked, the player moves to the previous room
        left.setOnMouseClicked(e -> handleRoomChange(controller, -1));

        // When the right button is clicked, the player moves to the next room
        right.setOnMouseClicked(e -> handleRoomChange(controller, 1));

        // When the enter button is clicked, the player moves to the first room
        enter.setOnMouseClicked(e -> handleRoomEnter(controller, manager));

        dPane.getChildren().add(buttons);
    }

    // when the room changes, the sector is highlighted
    private void handleRoomChange(final GameController controller, final int direction) {
        controller.changeRoom(direction);
        highlightSector(controller.getPlayerActualRoom());
    }

    private void handleRoomEnter(final GameController controller, final SceneManager manager) {
        highlightSector(controller.getPlayerActualRoom());
        manager.switchTo(controller.enterRoom());
    }

    private Circle createCircle(final String id, final double radius) {
        final Circle circle = new Circle(radius);
        circle.setId(id);
        return circle;
    }

    // Adapts the scene to the screen size
    private void adaptScene(final Pane scene, final GameController controller) {
        final double centerX = scene.getWidth() / 2;
        final double centerY = scene.getHeight() / 2;

        updateCirclePositionAndRadius(outer, centerX, centerY,
                Math.min(scene.getWidth(), scene.getHeight()) / 3);
        updateCirclePositionAndRadius(inner, centerX, centerY,
                Math.min(scene.getWidth(), scene.getHeight()) / INNER_CIRCLE_RATIO);

        final double roomRadius = (outer.getRadius() + inner.getRadius()) / 2;
        dPane.getChildren().removeIf(node -> node instanceof ImageView || node instanceof Arc || node instanceof Text
                || node instanceof Line || node instanceof HBox);

        buttons.setLayoutX(centerX - buttons.getPrefWidth() * 3 / 2);
        buttons.setLayoutY(scene.getHeight() / BUTTON_VERTICAL_POSITION);

        dPane.getChildren().add(buttons);
        sectorMap.clear();

        for (int i = 0; i < controller.getNumberOfRooms(); i++) {
            createRoomAndSector(controller, i, centerX, centerY, roomRadius, controller.isRoomToDisplay());
        }

        applyInnerCircleTexture();
        Platform.runLater(() -> highlightSector(controller.getPlayerActualRoom()));
    }

    private void applyInnerCircleTexture() {
        try {
            final URL imageUrl = MainFloorView.class.getResource("/Images/inner_circle_background.png");
            if (imageUrl != null) {
                inner.setFill(new ImagePattern(new Image(imageUrl.toExternalForm(), false)));
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed to load inner circle background image" + e);
        }
        inner.setEffect(new DropShadow(SHADOW_SPREAD, 0d, SHADOW_Y_OFFSET, Color.DARKSEAGREEN));
        inner.toFront();
    }

    private void updateCirclePositionAndRadius(final Circle circle, final double centerX,
            final double centerY, final double radius) {
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        circle.setRadius(radius);
    }

    private void createRoomAndSector(final GameController controller, final int roomIndex, final double centerX,
            final double centerY, final double roomRadius, final boolean isToDisplay) {
        final double angle = 2 * Math.PI / nRooms * roomIndex;
        final double x = centerX + roomRadius * Math.cos(angle) - SECTOR_ANGLE_OFFSET;
        final double y = centerY + roomRadius * Math.sin(angle) - SECTOR_ANGLE_OFFSET;

        dPane.getChildren().add(createRoomLabel(x, y, roomIndex));

        dPane.getChildren().add(addImage(controller, x, y, isToDisplay, roomIndex));

        final Arc sector = createSector(centerX, centerY, outer.getRadius(), roomIndex);

        // add the sector to the map for highlighting
        sectorMap.put(roomIndex, sector);
        dPane.getChildren().add(sector);

        dPane.getChildren().add(createDivisionLine(centerX, centerY, angle));
    }

    private ImageView addImage(final GameController controller, final double x, final double y, final boolean isToDisplay,
            final int roomIndex) {
        final String path;
        if (isToDisplay) {
            path = controller.getRoomImagePath(roomIndex);
        } else {
            path = "Images/smoke.gif";
        }
        final InputStream stream = getClass().getClassLoader()
                .getResourceAsStream(path != null
                        ? path
                        : "Images/smoke.gif");
        final ImageView spriteImg = new ImageView(new Image(stream));

        spriteImg.setTranslateX(x);
        spriteImg.setTranslateY(y);
        spriteImg.setFitHeight(100);
        spriteImg.setFitWidth(100);

        return spriteImg;
    }

    private void highlightSector(final int roomIndex) {
        // Reset all previous highlights
        sectorMap.values().forEach(sector -> sector.setFill(null));

        // Highlight the selected sector
        final Arc selectedSector = sectorMap.get(roomIndex);
        if (selectedSector != null) {
            LOGGER.info("Sector found, applying highlight.");
            selectedSector.setFill(HIGHLIGHT_COLOR);
            final FadeTransition fade = new FadeTransition(Duration.seconds(FADE_DURATION), selectedSector);
            fade.setFromValue(1.0);
            fade.setToValue(FADE_MIN_OPACITY);
            fade.setCycleCount(Animation.INDEFINITE);
            fade.setAutoReverse(true);
            fade.play();
        } else {
            LOGGER.warn("No sector found for room: " + roomIndex);
        }
    }

    private Text createRoomLabel(final double x, final double y, final int roomIndex) {
        final Text label = new Text(x + 10, y + ROOM_LABEL_OFFSET, "R" + (roomIndex + 1));
        label.setFill(Color.WHITE);
        return label;
    }

    private Arc createSector(final double centerX, final double centerY,
            final double outerRadius, final int roomIndex) {
        double startAngle = (nRooms - roomIndex - 1) * (360.0 / nRooms);
        startAngle = startAngle + ANGLE_OFFSET;
        final double sectorLength = 360.0 / nRooms;

        final Arc sector = new Arc(centerX, centerY, outerRadius, outerRadius, startAngle, sectorLength);
        sector.getStyleClass().add("sector");
        sector.setType(ArcType.ROUND);
        return sector;
    }

    private Line createDivisionLine(final double centerX, final double centerY, final double angle) {
        final double rotatedAngle = angle - 90;
        final double startX = centerX + inner.getRadius() * Math.cos(rotatedAngle + Math.PI / 2); // Cerchio interno
        final double startY = centerY + inner.getRadius() * Math.sin(rotatedAngle + Math.PI / 2);
        final double endX = centerX + outer.getRadius() * Math.cos(rotatedAngle + Math.PI / 2); // Cerchio esterno
        final double endY = centerY + outer.getRadius() * Math.sin(rotatedAngle + Math.PI / 2);

        final Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.WHITE);
        return line;
    }
}
