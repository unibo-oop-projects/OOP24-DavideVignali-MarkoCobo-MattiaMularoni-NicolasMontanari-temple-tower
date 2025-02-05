package it.unibo.templetower.model;

import java.util.List;
import java.util.Optional;

import it.unibo.templetower.utils.Pair;

/**
 * Record representing a floor's data in the game.
 * Contains information about the floor's name, sprite, enemies, weapons, spawn range and spawn weight.
 * Enemy and weapon lists are optional and may be empty if their respective files are
 * empty or invalid.
 *
 * @param floorName the name of the floor
 * @param spritePath path to the floor's sprite resource
 * @param enemies optional list of enemies that can spawn on this floor
 * @param weapons optional list of weapons that can be found on this floor
 * @param spawningRange pair containing min and max levels for floor generation
 * @param spawnWeight weight value affecting how likely this floor is to be selected during generation
 */
public record FloorData(
    String floorName,
    String spritePath,
    Optional<List<Enemy>> enemies,
    Optional<List<Weapon>> weapons,
    Pair<Integer, Integer> spawningRange,
    int spawnWeight
) {
    /**
     * Compact constructor for validation.
     * Ensures that required parameters are not null and spawnWeight is positive.
     * @throws IllegalArgumentException if required parameters are null or if spawnWeight is less than 1
     */
    public FloorData {
        if (floorName == null || spritePath == null || spawningRange == null) {
            throw new IllegalArgumentException("Required floor parameters cannot be null");
        }
        if (spawnWeight < 1) {
            throw new IllegalArgumentException("Spawn weight must be at least 1");
        }
        // If enemies or weapons are null, convert to Optional.empty()
        enemies = Optional.ofNullable(enemies).orElse(Optional.empty());
        weapons = Optional.ofNullable(weapons).orElse(Optional.empty());
    }
}
