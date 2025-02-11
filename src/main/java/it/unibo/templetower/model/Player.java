package it.unibo.templetower.model;

/**
 * Represents the Player in the game.
 */
public interface Player {
    /**
     * @param enemy
     */
    void attack(EnemyRoom enemy);

    /**
     * @param weapon
     */
    void changeWeapon(Weapon weapon);

    /**
     * @param damage
     * It can be called by the enemy or traps to deal damage to the player.
     */
    void takeDamage(double damage);

    /**
     * @param room
     * Change the actual room of the player.
     */
    void changeRoom(Room room);


    /**
     * @return the player's experience points.
     */
    int getExperience();

    /**
     * @param xp
     * It can be called by a treasure that contains experience points.
     */
    void increaseExperience(int xp);

    /**
     * @return the player's actual room.
     */
    int getActualRoom();

    /**
     * @return the player's current weapon.
     */
    Weapon getActualWeapon();

    /**
     * @return the player's current life points.
     */
    double getLife();
}
