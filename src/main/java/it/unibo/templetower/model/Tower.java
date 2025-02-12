package it.unibo.templetower.model;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a tower in the game with its properties.
 * Contains information about the tower's name, description, floors, and attack sprites.
 *
 * @param name The name of the tower
 * @param description A description of the tower
 * @param floors The list of floor data for the tower
 * @param attacksSprite The mapping of attack IDs to sprite paths
 * @param height The height of the tower in levels
 */
public record Tower(
    String name,
    String description,
    List<FloorData> floors,
    Map<String, String> attacksSprite,
    int height
) {
    /**
     * Creates a new Tower with defensive copies of mutable collections.
     */
    public Tower {
        floors = new ArrayList<>(floors);
        attacksSprite = new HashMap<>(attacksSprite);
    }

    @Override
    public List<FloorData> floors() {
        return new ArrayList<>(floors);
    }

    @Override
    public Map<String, String> attacksSprite() {
        return new HashMap<>(attacksSprite);
    }
}
