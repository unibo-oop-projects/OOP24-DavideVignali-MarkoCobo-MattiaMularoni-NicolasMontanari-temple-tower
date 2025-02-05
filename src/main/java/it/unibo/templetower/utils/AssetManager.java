package it.unibo.templetower.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Manager for put and get assets for all entities in the game.
 * In particular, it manages the assets for the enemies based on their strength level.
 */

public class AssetManager {
    private final NavigableMap<Integer, String> enemyAssets;
    private final Map<String, String> genericEntityAsset;

    public AssetManager(){
        this.enemyAssets = new TreeMap<>();
        this.genericEntityAsset = new HashMap<>();
    }

    public void addEnemyAsset(Integer level, String asset){
        enemyAssets.put(level, asset);
    }

    public void addGenericEntityAsset(String entity, String asset){
        genericEntityAsset.put(entity, asset);
    }

    public String getEnemyAsset(Integer level) {
        Integer closestLevel = enemyAssets.floorKey(level);

        // If there is no key <= level, return the first available asset
        if (closestLevel == null) {
            return enemyAssets.firstEntry().getValue();
        }
        return enemyAssets.get(closestLevel);
    }

    public String getGenericEntityAsset(String type){
        return genericEntityAsset.get(type);
    }
}
