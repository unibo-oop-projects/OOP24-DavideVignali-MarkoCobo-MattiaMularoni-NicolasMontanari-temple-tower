package it.unibo.templetower.model;

import java.util.List;

/**
 * Record representing a floor's in the game.
 * Contains information about the floor's name, sprite, enemies, weapons, spawn range, spawn weight and visibility.
 * Visibility (a double between 0 and 1) indicates the probability of viewing the floor's tiles.
 *
 * @param floorName the name of the floor
 * @param spritePath path to the floor's sprite resource
 * @param enemies optional list of enemies that can spawn on this floor
 * @param weapons optional list of weapons that can be found on this floor
 * @param spawningRange pair containing min and max levels for floor generation
 * @param spawnWeight weight value affecting how likely this floor is to be selected during generation
 * @param visibility probability (0 to 1) of viewing the floor's tiles
 */
public record Floor(
    String floorName,
    String spritePath,
    List<Room> rooms,
    double visibility
) {
    /**
     * Compact constructor for validation.
     * Ensures that required parameters are not null and spawnWeight is positive.
     * @throws IllegalArgumentException if required parameters are null or if spawnWeight is less than 1
     */
    public Floor {
        if (floorName == null || spritePath == null) {
            throw new IllegalArgumentException("Required floor parameters cannot be null");
        }
        if (visibility < 0.0 || visibility > 1.0) {
            throw new IllegalArgumentException("Visibility must be between 0 and 1");
        }
    }
}