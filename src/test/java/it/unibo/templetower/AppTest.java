/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package it.unibo.templetower;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unibo.templetower.controller.GameDataManagerImpl;
import it.unibo.templetower.model.FloorData;

class AppTest {

    @Test 
    void testAppHasAGreeting() throws ClassNotFoundException {
        assertNotNull(Class.forName("javafx.scene.Scene"));
    }

    @Test
    void testVerifyPath() {
        GameDataManagerImpl gameDataManager = new GameDataManagerImpl();
        String testPath = "tower/floors/floors-data.json";
        assertTrue(gameDataManager.verifyPath(testPath));
    }

    /**
     * Tests the loading and printing of floor data from the game configuration.
     * This test verifies that:
     * 1. The GameDataManager can successfully load floor data
     * 2. The loaded data contains at least one floor
     * 3. Prints the content of each floor to the console for verification
     */
    @Test
    void testLoadAndPrintFloorData() {
        GameDataManagerImpl gameDataManager = new GameDataManagerImpl();
        String testPath = "tower/floors/floors-data.json";
        
        // Load the game data
        gameDataManager.loadGameData(testPath);
        
        // Get the floors list
        List<FloorData> floors = gameDataManager.getFloors();
        
        // Verify that floors were loaded
        assertFalse(floors.isEmpty(), "Floor list should not be empty");
        
        // Print floor data
        printFloorDetails(floors);
    }

    /**
     * Helper method to print the details of each floor in a formatted way.
     * Prints floor name, sprite path, number of enemies, number of weapons,
     * and the level range for each floor. Safely handles null values in weapon data.
     *
     * @param floors the list of FloorData objects to print
     */
    private void printFloorDetails(List<FloorData> floors) {
        System.out.println("\n=== Floor Data Details ===");
        for (int i = 0; i < floors.size(); i++) {
            FloorData floor = floors.get(i);
            System.out.println("\nFloor #" + (i + 1));
            System.out.println("Name: " + floor.floorName());
            System.out.println("Sprite Path: " + floor.spritePath());
            System.out.println("Spawn Weight: " + floor.spawnWeight());
            System.out.println("Number of Enemies: " + 
                floor.enemies().map(List::size).orElse(0));
            System.out.println("Number of Weapons: " + 
                floor.weapons().map(List::size).orElse(0));
            System.out.println("Level Range: " + floor.spawningRange().getX() + 
                             " - " + floor.spawningRange().getY());
            
            // Print enemy details with attack IDs and sprite path
            System.out.println("\nEnemies:");
            floor.enemies().ifPresent(enemies -> 
                enemies.forEach(enemy -> {
                    System.out.println("- " + enemy.name() + " (Level " + enemy.level() + 
                                     ", Health: " + enemy.health() + ")");
                    System.out.println("  Sprite: " + enemy.spritePath());
                    enemy.attacks().forEach(attack -> 
                        System.out.println("  Attack: " + attack.getX() + " - Damage: " + attack.getY()));
                    System.out.println("  Damage Multipliers:");
                    enemy.damageMultipliers().forEach(multiplier ->
                        System.out.println("    " + multiplier.getX() + ": x" + multiplier.getY()));
                }));
            
            // Print weapon details with attack type, damage, level and sprite path
            System.out.println("\nWeapons:");
            floor.weapons().ifPresent(weapons -> 
                weapons.forEach(weapon -> {
                    String damage = Optional.ofNullable(weapon.attack())
                        .map(attack -> "Type: " + attack.getX() + ", Damage: " + attack.getY())
                        .orElse("N/A");
                    System.out.println("- " + weapon.name() + " (Level " + weapon.level() + ")");
                    System.out.println("  " + damage);
                    System.out.println("  Sprite: " + weapon.spritePath());
                }));
        }
        System.out.println("\n=== End of Floor Data ===\n");
    }

}
