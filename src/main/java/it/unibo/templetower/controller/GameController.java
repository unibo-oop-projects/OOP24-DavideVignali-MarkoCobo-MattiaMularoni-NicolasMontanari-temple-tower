package it.unibo.templetower.controller;

/**
 * Defines the core functionalities required to manage the game logic, 
 * including room navigation, game state management, and player actions.
 */
public interface GameController {

    /**
     * Starts the game, initializing necessary components and setting the initial state.
     */
    void startGame();

    /**
     * Ends the game, performing cleanup operations if necessary.
     */
    void endGame();

    /**
     * Handles a specific action performed by the player.
     *
     * @param action A string representing the action to be processed.
     */
    void handleAction(String action);

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
     * Returns the path of the sprite of the enemy for the specified level.
     * 
     * @param level the level number for which to get the enemy sprite
     * @return a String containing the file path to the enemy sprite
     */
    String getEnemySpritePath(int level);

    /** 
     * Returns the path of the sprite for the specified entity type.
     * 
     * @param type the type of entity for which to get the sprite
     * @return a String containing the file path to the entity sprite
     */
    String getEntiSpritePath(String type);

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
     * Returns the path of the sprite for the specified entity type.
     * 
     * @return a String containing the file path to the entity sprite
     */
    String getEnemySpritePath();
}
