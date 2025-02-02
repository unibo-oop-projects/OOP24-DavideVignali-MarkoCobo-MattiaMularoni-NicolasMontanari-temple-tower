package it.unibo.templetower.controller;

import java.util.List;

import it.unibo.templetower.model.Room;

public interface GameController {
    void startGame();
    void endGame();
    void handleAction(String action);
    void goToNextFloor();
    void changeRoom(Integer direction);
    void enterFirstRoom();
    int getPlayerActualRoom();
    List<Room> getRooms();
}
