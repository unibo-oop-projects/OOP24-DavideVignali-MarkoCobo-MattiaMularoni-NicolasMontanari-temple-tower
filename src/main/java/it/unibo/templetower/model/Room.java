package it.unibo.templetower.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * Useful class for implement the strategy pattern, 
 * it represents a generic room that can interact with the player.
 * This class is designed for extension to support different types of rooms.
 */
public final class Room {
    private static final Logger LOGGER = LoggerFactory.getLogger(Room.class);
    private final RoomBehavior behavior;
    private final String name;
    private final int id;

    /**
     * Creates a new Room with the specified behavior, name and id.
     * @param behavior the behavior of the room
     * @param name the name of the room
     * @param id the unique identifier of the room
     */
    public Room(final RoomBehavior behavior, final String name, final int id) {
        this.behavior = behavior;
        this.id = id;
        this.name = name;
    }

    /**
     * Handles the player entering the room.
     * @param player the player entering the room
     */
    public void enter(final Player player) {
        if (behavior != null) {
            player.changeRoom(this);
        } else {
            LOGGER.info("The room is empty.");
        }
    }

    /**
     * Handles player interaction with the room.
     * @param player the player interacting with the room
     * @param direction the direction of interaction
     */
    public void interactWithRoom(final Player player, final int direction) {
        if (behavior != null) {
            behavior.interact(player, direction);
        } else {
            LOGGER.info("The room is empty.");
        }
    }

    /**
     * Gets the life points of an enemy in the room if present.
     * @return enemy life points or -1 if no enemy is present
     */
    public Enemy getEnemy(){
        if (behavior instanceof EnemyRoom enemyRoom) {
            return enemyRoom.getEnemy();
        }
        return null;
    }

    public double getEnemyLife() {
        if (behavior instanceof EnemyRoom enemyRoom) {
            return enemyRoom.getLifePoints();
        }
        return -1; // Indicates that the room doesn't contain an enemy
    }

    /**
     * Gets the life points of the player.
     * @param player the player whose life points to get
     * @return the player's life points
     */
    public double getLifePlayer(final Player player) {
        return player.getLife();
    }

    /**
     * Gets the room's unique identifier.
     * @return the room's id
     */
    public String getEnemySprite(){
        if (behavior instanceof EnemyRoom enemyRoom) {
            return enemyRoom.getEnemyImagePath();
        }
        return null;
    }

    public int getId() {
        return id;
    }

    /**
     * Gets the room's name.
     * @return the room's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the room's behavior.
     * @return the room's behavior
     */
    public RoomBehavior getBehavior() {
        return behavior;
    }
}
