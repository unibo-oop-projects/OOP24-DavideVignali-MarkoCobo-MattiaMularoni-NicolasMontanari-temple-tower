package it.unibo.templetower.model;

import java.util.ArrayList;
import java.util.List;

public class SpawnManagerImpl {
    private List<FloorData> floors;

    public SpawnManagerImpl(List<FloorData> floors){
        this.floors = new ArrayList<>(floors);
    }
    /**
     * If the method is called without specifying the number of number it is assumed to be an arbitrary standard
     *
     * @param level the current level for the floor selection
     * @return the Floor with all the rooms generated
     */
    public Floor spawnFloor(int level){
       return spawnFloor(level,7);
    }

    public Floor spawnFloor(int level, int roomNumber){
        FloorData generatedFloor = selectFloortype(level);
       return null;
    }

    /**
     * Chooses a FloorData based on the given level, taking its spawnWeight into account in a weighted manner.
     * Filters floors that can spawn at the specified level by checking the spawning range,
     * and uses the spawn weight to randomly select one of the eligible floors.
     *
     * @param level the current level for the floor selection
     * @return the FloorData selected based on the level and its spawnWeight, or null if no eligible floor is found
     */
    private FloorData selectFloortype(int level){
        // Filter floors with a spawning range that includes the given level
        List<FloorData> eligibleFloors = floors.stream()
            .filter(fd -> {
                var range = fd.spawningRange();
                return level >= range.getX() && level <= range.getY();
            })
            .toList();
        
        if(eligibleFloors.isEmpty()){
            return null;
        }
        
        // Calculate the total weight of the eligible floors
        int totalWeight = eligibleFloors.stream().mapToInt(FloorData::spawnWeight).sum();
        double r = Math.random() * totalWeight;
        int cumulative = 0;
        
        // Weighted random selection based on spawn weight
        for (FloorData fd : eligibleFloors) {
            cumulative += fd.spawnWeight();
            if(r < cumulative){
                return fd;
            }
        }
        // Fallback, should rarely occur
        return eligibleFloors.get(eligibleFloors.size() - 1);
    }
}
