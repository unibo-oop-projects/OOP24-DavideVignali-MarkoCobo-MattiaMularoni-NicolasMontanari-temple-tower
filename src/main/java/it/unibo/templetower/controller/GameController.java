package it.unibo.templetower.controller;

public interface GameController {
    void startGame();
    void endGame();
    void handleAction(String action);
    void goToNextFloor();
    void changeRoom(Integer direction);
}
