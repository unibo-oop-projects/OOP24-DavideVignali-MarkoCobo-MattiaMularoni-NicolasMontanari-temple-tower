package it.unibo.templetower.view;

import it.unibo.templetower.model.RoomBehavior;

public interface GameView {
    void displayRoom(RoomBehavior room);
    void updateStatusBar(int health, int experience);
    void showGameOver();
    void showVictory();
}
