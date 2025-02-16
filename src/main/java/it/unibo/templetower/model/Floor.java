package it.unibo.templetower.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Record representing a floor's in the game.
 * Contains information about the floor's name, sprite, enemies, weapons, spawn range, spawn weight and visibility.
 * 
 * @param floorName the name of the floor
 * @param spritePath path to the floor's sprite resource
 * @param rooms list of rooms in the floor
 * @param visibility probability (0 to 1) of viewing the floor's tiles
 */
public record Floor(
    String floorName,
    String spritePath,
    List<Room> rooms,
    double visibility) {
    /**
     * Compact constructor for validation.
     * Ensures that required parameters are not null and spawnWeight is positive.
     * @throws IllegalArgumentException if required parameters are null or if spawnWeight is less than 1
     */
    public Floor {
        if (floorName == null || spritePath == null || rooms == null) {
            throw new IllegalArgumentException("Required floor parameters cannot be null");
        }
        if (visibility < 0.0 || visibility > 1.0) {
            throw new IllegalArgumentException("Visibility must be between 0 and 1");
        }
        // Create defensive copy of mutable list
        rooms = new ArrayList<>(rooms);
    }

    @Override
    public List<Room> rooms() {
        return new ArrayList<>(rooms);
    }
}
