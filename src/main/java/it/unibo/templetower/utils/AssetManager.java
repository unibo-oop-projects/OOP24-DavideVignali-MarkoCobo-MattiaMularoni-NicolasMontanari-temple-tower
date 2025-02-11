package it.unibo.templetower.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Manager for put and get assets for all entities in the game.
 * In particular, it manages the assets for the enemies based on their strength level.
 * This class is not designed for extension.
 */
public final class AssetManager {
    private final NavigableMap<Integer, String> enemyAssets;
    private final Map<String, String> genericEntityAsset;

    /**
     * Constructs a new AssetManager with empty asset maps.
     */
    public AssetManager() {
        this.enemyAssets = new TreeMap<>();
        this.genericEntityAsset = new HashMap<>();
    }

    /**
     * Adds an asset for enemies of a specific level.
     * @param level the enemy's strength level
     * @param asset the path to the asset
     */
    public void addEnemyAsset(final Integer level, final String asset) {
        enemyAssets.put(level, asset);
    }

    /**
     * Adds an asset for a generic entity type.
     * @param entity the entity type
     * @param asset the path to the asset
     */
    public void addGenericEntityAsset(final String entity, final String asset) {
        genericEntityAsset.put(entity, asset);
    }

    /**
     * Gets the appropriate asset for an enemy of the specified level.
     * @param level the enemy's strength level
     * @return the path to the appropriate asset
     */
    public String getEnemyAsset(final Integer level) {
        Integer closestLevel = enemyAssets.floorKey(level);

        // If there is no key <= level, return the first available asset
        if (closestLevel == null) {
            return enemyAssets.firstEntry().getValue();
        }
        return enemyAssets.get(closestLevel);
    }

    /**
     * Gets the asset for a specific entity type.
     * @param type the entity type
     * @return the path to the entity's asset
     */
    public String getGenericEntityAsset(final String type) {
        return genericEntityAsset.get(type);
    }
}
