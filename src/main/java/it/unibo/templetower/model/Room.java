package it.unibo.templetower.model;

/** 
 * Useful class for implement the strategy pattern, 
 * it represents a generic room that can interact with the player.
 */
public class Room {
    private final RoomBehavior behavior;
    protected final String name;
    protected final int id;

    public Room(RoomBehavior behavior, final String name, final int id) {
        this.behavior = behavior;
        this.id = id;
        this.name = name;
    }

    public void enter(Player player) {
        if (behavior != null) {
            player.changeRoom(this);
        } else {
            System.out.println("The room is empty.");
        }
    }

    public void interactWithRoom(Player player, int direction) {
        if (behavior != null) {
            behavior.interact(player, direction);
        } else {
            System.out.println("The room is empty.");
        }
    }

    public double getEnemyLife() {
        if (behavior instanceof EnemyRoom enemyRoom) {
            return enemyRoom.getLifePoints();
        }
        return -1; // Indica che la stanza non contiene un nemico
    }

    public double getLifePlayer(Player player){
        return player.getLife();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RoomBehavior getBehavior(){
        return behavior;
    }

}