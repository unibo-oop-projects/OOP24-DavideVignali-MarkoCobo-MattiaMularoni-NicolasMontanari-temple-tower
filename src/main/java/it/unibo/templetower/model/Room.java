package it.unibo.templetower.model;

/** 
 * Useful class for implement the strategy pattern, 
 * it represents a generic room that can interact with the player.
 */
public class Room {
    private final RoomBehavior behavior;
    protected final int id;

    public Room(RoomBehavior behavior, final int id) {
        this.behavior = behavior;
        this.id = id;
    }

    public void enter(Player player) {
        if (behavior != null) {
            player.changeRoom(this);
            behavior.interact(player, 0);
        } else {
            System.out.println("The room is empty.");
        }
    }

    public void attack(Player player) {
        behavior.interact(player, 1);
    }

    public int getId() {
        return id;
    }

    public RoomBehavior getBehavior(){
        return behavior;
    }
}