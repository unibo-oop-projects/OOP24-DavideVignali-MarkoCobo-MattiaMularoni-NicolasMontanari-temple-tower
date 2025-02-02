package it.unibo.templetower.model;

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
            behavior.interact(player);
        } else {
            System.out.println("The room is empty.");
        }
    }

    public int getId() {
        return id;
    }
}