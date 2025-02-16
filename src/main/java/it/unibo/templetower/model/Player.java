package it.unibo.templetower.model;

import java.util.List;

/**
 * Represents the Player in the game.
 */
public interface Player {
    /**
     * @param diff
     */
    void setDifficulty(double diff);

    /**
     * @param enemy
     */
    void attack(EnemyRoom enemy);

    /**
     * @param index
     */
    void changeWeapon(int index);

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

    /**
     * Resets the player's life to 100 after a battle.
     */
    void resetLife();

    /**
     * Adds the specified weapon to the player's weapon list.
     * 
     * @param newWeapon the weapon to be added
     * @param index the position at which the weapon will be added in the list
     */
    void addWeapon(Weapon newWeapon, int index);

    /**
     * Retrieves all weapons in the player's inventory.
     * 
     * @return a list of all weapons
     */
    List<Weapon> getAllWeapons();

}
