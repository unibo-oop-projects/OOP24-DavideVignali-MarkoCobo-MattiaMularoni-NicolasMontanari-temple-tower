package it.unibo.templetower.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import it.unibo.templetower.utils.EnemyGenerator;

public class SpawnManagerImpl {
    // New probability constants
    private static final double ENEMY_ROOM_CHANCE = 0.5;
    private static final double EMPTY_ROOM_CHANCE = 0.25;
    private static final double TREASURE_ROOM_CHANCE = 0.125;

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

    /**
     * Spawns a floor with a given number of rooms.
     * One room is randomly set as a StairsRoom.
     * The remaining rooms are generated based on arbitrary probability.
     * For enemy rooms, enemies are generated using a budget mechanism:
     *
     * @param level the current floor level
     * @param roomNumber the total number of rooms to spawn (including the StairsRoom)
     * @return a Floor with all the rooms generated
     */
    public Floor spawnFloor(int level, int roomNumber){
        FloorData generatedFloor = selectFloortype(level);
        List<Room> generatedRooms = new ArrayList<>();
        Random random = new Random();
        int stairsIndex = random.nextInt(roomNumber);
        int enemyBudget = level * 5; // initialize budget

        for (int i = 0; i < roomNumber; i++) {
            if(i == stairsIndex) {
                generatedRooms.add(new Room(new StairsRoom(), "stairs_view", i));
            } else {
                double roll = random.nextDouble();
                if(roll < ENEMY_ROOM_CHANCE) {
                    // Enemy room: 50%
                    var enemies = generatedFloor.enemies().orElse(Collections.emptyList());
                    if(enemies.isEmpty()){
                        generatedRooms.add(new Room(null, "empty_view", i));
                    } else {
                        Enemy selectedEnemy = EnemyGenerator.pickEnemyByBudget(enemies, enemyBudget, random);
                        enemyBudget = Math.max(1, enemyBudget - selectedEnemy.level());
                        generatedRooms.add(new Room(new EnemyRoom(selectedEnemy), "combat_view", i));
                    }
                } else if(roll < ENEMY_ROOM_CHANCE + EMPTY_ROOM_CHANCE) {
                    // Empty room: 25%
                    generatedRooms.add(new Room( null, "empty_view", i));
                } else if(roll < ENEMY_ROOM_CHANCE + EMPTY_ROOM_CHANCE + TREASURE_ROOM_CHANCE) {
                    // Treasure room: 12.5%
                    var weapons = generatedFloor.weapons().orElse(Collections.emptyList());
                    Optional<Weapon> randomWeapon = weapons.isEmpty() 
                        ? Optional.empty() 
                        : Optional.of(weapons.get(random.nextInt(weapons.size())));
                    generatedRooms.add(new Room(new TreasureRoom(level, randomWeapon, 0.5, 0.1, 0.4), "treasure_view", i));
                } else {
                    // 12.5% chance: Trap room with damage = 1
                    generatedRooms.add(new Room(new Trap(1), "trap_view", i));
                }
            }
        }
        return new Floor(generatedFloor.floorName(), generatedFloor.spritePath(), generatedRooms, generatedFloor.visibility());
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
