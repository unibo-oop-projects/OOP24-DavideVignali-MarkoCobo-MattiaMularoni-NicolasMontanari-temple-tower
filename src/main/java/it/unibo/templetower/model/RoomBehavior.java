package it.unibo.templetower.model;

public interface RoomBehavior {
    /**
     * 
     */
    void generateContent();

    /**
     * @param player
     */
    void interact(Player player);

}
