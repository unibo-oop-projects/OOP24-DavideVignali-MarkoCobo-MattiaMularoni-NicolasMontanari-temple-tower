package templeTower.controller;

import javafx.scene.control.skin.TextInputControlSkin.Direction;

public interface GameController {
    void startGame();
    void endGame();
    void handleAction(String action);
    void goToNextFloor();
    void changeRoom(Direction direction);
}
