package it.unibo.templetower.model;

public interface Tower {
    void generateLevels();
    RoomBehavior getCurrentRoom();
    void movePlayer(int floorIndex);
}
