package it.unibo.templetower.util;

import java.util.List;
import java.util.Optional;
import it.unibo.templetower.model.FloorData;

public class FloorPrinterUtil {

    public static void printFloorDetails(List<FloorData> floors) {
        System.out.println("\n=== Floor Data Details ===");
        for (int i = 0; i < floors.size(); i++) {
            FloorData floor = floors.get(i);
            System.out.println("\nFloor #" + (i + 1));
            System.out.println("Name: " + floor.floorName());
            System.out.println("Sprite Path: " + floor.spritePath());
            System.out.println("Spawn Weight: " + floor.spawnWeight());
            System.out.println("Visibility: " + floor.visibility());
            System.out.println("Number of Enemies: " + floor.enemies().map(List::size).orElse(0));
            System.out.println("Number of Weapons: " + floor.weapons().map(List::size).orElse(0));
            System.out.println("Level Range: " + floor.spawningRange().getX() 
                                + " - " + floor.spawningRange().getY());

            // Print enemy details with attack IDs and sprite path
            System.out.println("\nEnemies:");
            floor.enemies().ifPresent(enemies ->
                enemies.forEach(enemy -> {
                    System.out.println("- " + enemy.name() + " (Level " + enemy.level() 
                        + ", Health: " + enemy.health() + ")");
                    System.out.println("  Sprite: " + enemy.spritePath());
                    enemy.attacks().forEach(attack -> 
                        System.out.println("  Attack: " + attack.getX() + " - Damage: " + attack.getY()));
                    System.out.println("  Damage Multipliers:");
                    enemy.damageMultipliers().forEach((attackId, multiplier) ->
                        System.out.println("    " + attackId + ": x" + multiplier));
                })
            );

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
                })
            );
        }
        System.out.println("\n=== End of Floor Data ===\n");
    }
}
