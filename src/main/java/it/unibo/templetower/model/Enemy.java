package it.unibo.templetower.model;

import java.util.List;

/**
 * Record representing an enemy in the game.
 * This record stores the basic information about an enemy including its name,
 * health points, level, list of possible attacks, and sprite path.
 */
public record Enemy(String name, Double health, int level, List<Pair<String,Double>> attacks, String spritePath) {
    /**
     * Compact constructor for validation.
     * Ensures that no null values are passed to the record.
     * @throws IllegalArgumentException if any parameter is null
     */
    public Enemy {
        if (name == null || health == null || attacks == null || spritePath == null) {
            throw new IllegalArgumentException("Enemy parameters cannot be null");
        }
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be positive");
        }
        if (level < 0) {
            throw new IllegalArgumentException("Level cannot be negative");
        }
    }
}