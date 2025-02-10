package it.unibo.templetower.model;

/**
 * Using strategy, is possible to extend this class, creating different action by only ovveride the {@code interact} method,
 * in this way is possible to create different behavior for the room, like a room with a chest, a room with a monster, a room with a trap, etc.
 */
public interface RoomBehavior {
    /**
     * 
     */
    void generateContent();

    /**
     * @param player
     */
    void interact(Player player, int direction);

}
