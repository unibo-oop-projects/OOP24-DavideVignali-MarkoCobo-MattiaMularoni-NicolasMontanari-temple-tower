package it.unibo.templetower.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import it.unibo.templetower.utils.EnemyGenerator;

/**
 * Manages the spawning of floors and rooms in the temple tower.
 */
public class SpawnManager {
    private static final double ENEMY_ROOM_CHANCE = 0.5;
    private static final double EMPTY_ROOM_CHANCE = 0.25;
    private static final double TREASURE_ROOM_CHANCE = 0.125;
    private static final int DEFAULT_ROOM_NUMBER = 7;
    private static final int BUDGET_MULTIPLIER = 5;
    private static final double TREASURE_HEALTH_CHANCE = 0.5;
    private static final double TREASURE_WEAPON_CHANCE = 0.5;
    private static final double TRAP_BASE_DAMAGE = 1.5;

    private final List<FloorData> floors;
    private final Random random;
    private final int towerHeight;

    private final int floorBeforeBoss;
    private int passedFloors;

    /**
     * Creates a new SpawnManager using the tower configuration.
     *
     * @param towerData the tower record holding floor data among other info
     */
    public SpawnManager(final Tower towerData) {
        this.passedFloors = 0;
        this.floors = new ArrayList<>(towerData.floors());
        this.random = new Random();
        this.towerHeight = towerData.height();
        floorBeforeBoss = towerData.height();
    }

    /**
     * Spawns a floor with the default number of rooms.
     *
     * @param level the current level for the floor selection
     * @return the Floor with all the rooms generated
     */
    public Floor spawnFloor(final int level) {
        return spawnFloor(level, DEFAULT_ROOM_NUMBER);
    }

    /**
     * Spawns a floor with a given number of rooms.
     * One room is randomly set as a StairsRoom.
     * The remaining rooms are generated based on arbitrary probability.
     * For enemy rooms, enemies are generated using a budget mechanism.
     *
     * @param level the current floor level
     * @param roomNumber the total number of rooms to spawn (including the StairsRoom)
     * @return a Floor with all the rooms generated
     */
    public Floor spawnFloor(final int level, final int roomNumber) {
        passedFloors++;
        final FloorData generatedFloor = selectFloortype(level);
        final List<Room> generatedRooms = new ArrayList<>();
        final int stairsIndex = random.nextInt(roomNumber);
        int enemyBudget = level * BUDGET_MULTIPLIER;

        if (passedFloors >= floorBeforeBoss) {
            final var enemies = generatedFloor.enemies().orElse(Collections.emptyList());
            final Enemy selectedEnemy = EnemyGenerator.pickEnemyByBudget(enemies, enemyBudget, random);
            return new Floor("boss", "images/boss.png", List.of(new Room(new EnemyRoom(selectedEnemy), "boss_view", 1)), 1);
        }

        for (int i = 0; i < roomNumber; i++) {
            if (i == stairsIndex) {
                generatedRooms.add(new Room(new StairsRoom(), "stairs_view", i));
            } else {
                final double roll = random.nextDouble();
                if (roll < ENEMY_ROOM_CHANCE) {
                    final var enemies = generatedFloor.enemies().orElse(Collections.emptyList());
                    if (enemies.isEmpty()) {
                        generatedRooms.add(new Room(null, "empty_view", i));
                    } else {
                        final Enemy selectedEnemy = EnemyGenerator.pickEnemyByBudget(enemies, enemyBudget, random);
                        enemyBudget = Math.max(1, enemyBudget - selectedEnemy.level());
                        generatedRooms.add(new Room(new EnemyRoom(selectedEnemy), "combat_view", i));
                    }
                } else if (roll < ENEMY_ROOM_CHANCE + EMPTY_ROOM_CHANCE) {
                    generatedRooms.add(new Room(null, "empty_view", i));
                } else if (roll < ENEMY_ROOM_CHANCE + EMPTY_ROOM_CHANCE + TREASURE_ROOM_CHANCE) {
                    final var weapons = generatedFloor.weapons().orElse(Collections.emptyList());
                    final Optional<Weapon> randomWeapon = weapons.isEmpty() 
                        ? Optional.empty() 
                        : Optional.of(weapons.get(random.nextInt(weapons.size())));
                    generatedRooms.add(new Room(new TreasureRoom(randomWeapon, 
                        TREASURE_HEALTH_CHANCE, TREASURE_WEAPON_CHANCE), "treasure_view", i));
                } else {
                    generatedRooms.add(new Room(new Trap(TRAP_BASE_DAMAGE * level), "trap_view", i));
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
    private FloorData selectFloortype(final int level) {
        if (level < 1 || level > towerHeight) {
            return null;
        }

        final List<FloorData> eligibleFloors = floors.stream()
            .filter(fd -> {
                final var range = fd.spawningRange();
                return level >= range.getX() && level <= range.getY();
            })
            .toList();

        if (eligibleFloors.isEmpty()) {
            return null;
        }

        final int totalWeight = eligibleFloors.stream().mapToInt(FloorData::spawnWeight).sum();
        final double r = Math.random() * totalWeight;
        int cumulative = 0;
        for (final FloorData fd : eligibleFloors) {
            cumulative += fd.spawnWeight();
            if (r < cumulative) {
                return fd;
            }
        }
        return eligibleFloors.get(eligibleFloors.size() - 1);
    }
}
