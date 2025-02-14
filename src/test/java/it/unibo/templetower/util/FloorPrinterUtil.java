package it.unibo.templetower.util;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.ConsoleHandler;
import it.unibo.templetower.model.FloorData;

/**
 * Utility class for printing floor details in a formatted way.
 */
public final class FloorPrinterUtil {

    private static final Logger LOGGER = Logger.getLogger(FloorPrinterUtil.class.getName());

    static {
        LOGGER.setUseParentHandlers(false);
        final ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(final java.util.logging.LogRecord record) {
                return record.getMessage() + "\n";
            }
        });
        LOGGER.addHandler(handler);
    }

    private FloorPrinterUtil() {
        // Utility class constructor
    }

    /**
     * Prints detailed information about a list of floors.
     *
     * @param floors the list of floor data to print
     */
    public static void printFloorDetails(final List<FloorData> floors) {
        for (int i = 0; i < floors.size(); i++) {
            final FloorData floor = floors.get(i);
            LOGGER.info("\nFloor #" + (i + 1));
            LOGGER.info("Name: " + floor.floorName());
            LOGGER.info("Sprite Path: " + floor.spritePath());
            LOGGER.info("Spawn Weight: " + floor.spawnWeight());
            LOGGER.info("Visibility: " + floor.visibility());
            LOGGER.info("Number of Enemies: " + floor.enemies().map(List::size).orElse(0));
            LOGGER.info("Number of Weapons: " + floor.weapons().map(List::size).orElse(0));
            LOGGER.info("Level Range: " + floor.spawningRange().getX() 
                                + " - " + floor.spawningRange().getY());

            // Print enemy details with attack IDs and sprite path
            LOGGER.info("\nEnemies:");
            floor.enemies().ifPresent(enemies ->
                enemies.forEach(enemy -> {
                    LOGGER.info("- " + enemy.name() + " (Level " + enemy.level() 
                        + ", Health: " + enemy.health() + ")");
                    LOGGER.info("  Sprite: " + enemy.spritePath());
                    enemy.attacks().forEach(attack -> 
                        LOGGER.info("  Attack: " + attack.getX() + " - Damage: " + attack.getY()));
                    LOGGER.info("  Damage Multipliers:");
                    enemy.damageMultipliers().forEach((attackId, multiplier) ->
                        LOGGER.info("    " + attackId + ": x" + multiplier));
                })
            );

            // Print weapon details with attack type, damage, level and sprite path
            LOGGER.info("\nWeapons:");
            floor.weapons().ifPresent(weapons ->
                weapons.forEach(weapon -> {
                    final String damage = Optional.ofNullable(weapon.attack())
                        .map(attack -> "Type: " + attack.getX() + ", Damage: " + attack.getY())
                        .orElse("N/A");
                    LOGGER.info("- " + weapon.name() + " (Level " + weapon.level() + ")");
                    LOGGER.info("  " + damage);
                    LOGGER.info("  Sprite: " + weapon.spritePath());
                })
            );
        }
    }
}
