package it.unibo.templetower.model;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import it.unibo.templetower.utils.Pair;

/**
 * Record representing an enemy in the game.
 * This record stores the basic information about an enemy including its name,
 * health points, level, list of possible attacks, damage multipliers for different attack types as a map,
 * and sprite path.
 * 
 * @param name the name of the enemy
 * @param health the health points of the enemy
 * @param level the level of the enemy
 * @param attacks the list of possible attacks of the enemy
 * @param damageMultipliers the damage multipliers for different attack types
 * @param spritePath the file path to the enemy's sprite
 */
public record Enemy(
    String name, 
    Double health, 
    int level, 
    List<Pair<String, Double>> attacks, 
    Map<String, Double> damageMultipliers, 
    String spritePath) {
    /**
     * Compact constructor for validation.
     * Ensures that no null values are passed to the record and creates defensive copies of mutable objects.
     * @throws IllegalArgumentException if any parameter is null
     */
    public Enemy {
        if (name == null || health == null || attacks == null || damageMultipliers == null || spritePath == null) {
            throw new IllegalArgumentException("Enemy parameters cannot be null");
        }
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be positive");
        }
        if (level < 0) {
            throw new IllegalArgumentException("Level cannot be negative");
        }
        // Create defensive copies of mutable objects
        attacks = new ArrayList<>(attacks);
        damageMultipliers = new HashMap<>(damageMultipliers);
    }

    /**
     * Returns a defensive copy of the attacks list.
     * @return a new list containing the attacks
     */
    @Override
    public List<Pair<String, Double>> attacks() {
        return new ArrayList<>(attacks);
    }

    /**
     * Returns a defensive copy of the damage multipliers map.
     * @return a new map containing the damage multipliers
     */
    @Override
    public Map<String, Double> damageMultipliers() {
        return new HashMap<>(damageMultipliers);
    }
}
