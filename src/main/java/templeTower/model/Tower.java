package templeTower.model;

public interface Tower {
    void generateLevels();
    RoomBehavior getCurrentRoom();
    void movePlayer();
}
