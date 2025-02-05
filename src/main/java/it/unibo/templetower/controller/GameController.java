package it.unibo.templetower.controller;

import java.util.List;

import it.unibo.templetower.model.Room;

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
     */
    void enterFirstRoom();

    /**
     * Gets the index of the room where the player is currently located.
     *
     * @return The index of the current room.
     */
    int getPlayerActualRoom();

    /**
     * Retrieves the list of rooms available on the current floor.
     *
     * @return A list of {@link Room} objects representing the rooms in the current floor.
     */
    List<Room> getRooms();

    /** 
     * Return the path of the sprite of the enemy of the given level.
     */
    String getEnemySpritePath(int level);

    /** 
     * Return the path of the sprite of the enemy of the given level.
     */
    String getEntiSpritePath(String type);
}
