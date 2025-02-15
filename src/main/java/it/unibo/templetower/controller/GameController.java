package it.unibo.templetower.controller;

import java.util.List;

import it.unibo.templetower.model.Weapon;

/**
 * Defines the core functionalities required to manage the game logic, 
 * including room navigation, game state management, and player actions.
 */
public interface GameController {

    /**
     * Moves the player to the next floor in the game.
     */
    void goToNextFloor();

    /**
     * Changes the player's current room based on the given direction.
     *
     * @param direction An integer representing the movement direction (e.g., -1 for left, 1 for right).
     */
    void changeRoom(Integer direction);

    /**
     * Moves the player to the first room of the current floor.
     *
     * @return a String representing the room entered
     */
    String enterRoom();

    /**
     * Gets the index of the room where the player is currently located.
     *
     * @return The index of the current room.
     */
    int getPlayerActualRoom();

    /**
     * Retrieves the list of rooms available on the current floor.
     *
     * @return The number of rooms in the current floor.
     */
    int getNumberOfRooms();

    /**
     * Performs an attack action from the player towards the enemy.
     */
    void attackEnemy();

    /**
     * Performs an attack action from the enemy towards the player.
     */
    void attackPlayer();

    /**
     * Gets the current life points of the player.
     * 
     * @return the current life points of the player as a double value
     */
    double getPlayerLife();

    /**
     * Gets the current life points of the enemy.
     * 
     * @return the current life points of the enemy as a double value
     */
    double getEnemyLifePoints();

    /** 
     * 
     * @param index of room
     * @return the room image path for displaying in the view
     */
    String getRoomImagePath(int index);

    /** 
     * When the player health is 0 reset the game.
     * 
     */
    void resetGame();

    /**
     * Simulates the damage taken by the player.
     */
    void playerTakeDamage();

    /**
     * Resets the player's life points to the initial value.
     */
    void resetPlayerLife();

    /**
     * Removes a weapon from the player's inventory.
     * 
     * @param index the index of the weapon to be removed
     */
    void removeWeapon(int index);

    /**
     * Add a weapon from the player's inventory.
     * 
     * @param newWeapon
     * @param index the index of the weapon to be removed
     */
    void addPlayerWeapon(Weapon newWeapon, int index);

    /**
     * Change a weapon from the player's inventory.
     * 
     * @param index the index of the weapon to be removed
     */
    void changeWeaponIndex(int index);

    /**
     * Retrieves the treasure element in the current room.
     * 
     * @return the treasure element in the room
     */
    int getElementTreasure();

    /**
     * Retrieves the treasure weapon in the current room.
     * 
     * @return the treasure weapon
     */
    Weapon getTreasureWeapon();

    /**
     * Retrieves the current player.
     * 
     * @return the player
     */
    List<Weapon> getPlayerWeapons();

    /**
     * Increases the player's life based on the given experience points (XP).
     *
     * @param xp The amount of experience points to be converted into life.
     */
    void increaseLifePlayer(int xp);

    /**
     * Retrieves the amount of experience points (XP) contained in the treasure.
     *
     * @return The XP value of the treasure.
     */
    int getXpTreasure();

    /**
     * 
     * @return if the room can be displayed or hidden
     */
    Boolean isRoomToDisplay();

    /**
     * 
     * @return if is time to show the boss view
     */
    Boolean isBossTime();

    /**
     * 
     * @return enemy image path
     */
    String getEnemyPath();

    /**
     * 
     * @return weapon image path
     */
    String getWeaponPath();

    /**
     * 
     * @return background floor image path
     */
    String getBackgroundImage();
}
