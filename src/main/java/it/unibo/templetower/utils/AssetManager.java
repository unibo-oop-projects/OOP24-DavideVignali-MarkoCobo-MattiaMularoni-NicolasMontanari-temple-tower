package it.unibo.templetower.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager for put and get assets for all entities in the game.
 * In particular, it manages the assets for the enemies based on their strength level.
 * This class is not designed for extension.
 */
public final class AssetManager {
    private final Map<String, String> genericEntityAsset;

    /**
     * Constructs a new AssetManager with empty asset maps.
     */
    public AssetManager() {
        this.genericEntityAsset = new HashMap<>();
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
     * Gets the asset for a specific entity type.
     * @param type the entity type
     * @return the path to the entity's asset
     */
    public String getGenericEntityAsset(final String type) {
        return genericEntityAsset.get(type);
    }
}
