package it.unibo.templetower.controller;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import it.unibo.templetower.model.Enemy;
import it.unibo.templetower.model.FloorData;
import it.unibo.templetower.model.Weapon;
import it.unibo.templetower.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Game Data Manager that handles loading and verification of game data from JSON files.
 * This class is responsible for loading and managing floor configurations, including their associated
 * enemies and weapons data from JSON configuration files.
 */
public final class GameDataManagerImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameDataManagerImpl.class);
    private final List<FloorData> floors;
    private String floorsPath;
    private final Gson gson;
    private static final int DEFAULT_TOWER_HEIGHT = 20;

    /**
     * Creates a new GameDataManagerImpl instance with an empty floors list and configured Gson instance.
     * The Gson instance is configured with custom deserializers for Enemy and Weapon classes to properly
     * handle the attack data structures from JSON.
     */
    public GameDataManagerImpl() {
        this.floors = new ArrayList<>();

        // Create custom deserializer for Enemy class
        final JsonDeserializer<Enemy> enemyDeserializer = (json, typeOfT, context) -> {
            final JsonObject jsonObject = json.getAsJsonObject();
            final String name = jsonObject.get("name").getAsString();
            final Double health = jsonObject.get("health").getAsDouble();
            final int level = jsonObject.get("level").getAsInt();
            final String spritePath = jsonObject.get("spritePath").getAsString();

            final List<Pair<String, Double>> attacks = new ArrayList<>();
            final JsonArray attacksArray = jsonObject.getAsJsonArray("attacks");
            for (final JsonElement attackElement : attacksArray) {
                final JsonObject attackObj = attackElement.getAsJsonObject();
                final String attackId = attackObj.get("attackId").getAsString();
                final Double damage = attackObj.get("damage").getAsDouble();
                attacks.add(new Pair<>(attackId, damage));
            }

            final Map<String, Double> multipliersMap = new HashMap<>();
            final JsonArray multipliersArray = jsonObject.getAsJsonArray("damageMultipliers");
            if (multipliersArray != null) {
                for (final JsonElement multiplierElement : multipliersArray) {
                    final JsonObject multiplierObj = multiplierElement.getAsJsonObject();
                    final String attackId = multiplierObj.get("attackId").getAsString();
                    final Double multiplier = multiplierObj.get("multiplier").getAsDouble();
                    multipliersMap.put(attackId, multiplier);
                }
            }

            return new Enemy(name, health, level, attacks, multipliersMap, spritePath);
        };

        // Custom deserializer for Weapon class
        final JsonDeserializer<Weapon> weaponDeserializer = (json, typeOfT, context) -> {
            final JsonObject jsonObject = json.getAsJsonObject();
            final String name = jsonObject.get("name").getAsString();
            final Integer level = jsonObject.get("level").getAsInt();
            final String spritePath = jsonObject.get("spritePath").getAsString();

            final JsonArray attacksArray = jsonObject.getAsJsonArray("attacks");
            final JsonObject attackObj = attacksArray.get(0).getAsJsonObject();
            final String attackId = attackObj.get("attackId").getAsString();
            final Double damage = attackObj.get("damage").getAsDouble();

            return new Weapon(name, level, new Pair<>(attackId, damage), spritePath);
        };

        // Configure Gson with custom deserializers
        this.gson = new GsonBuilder()
            .registerTypeAdapter(Enemy.class, enemyDeserializer)
            .registerTypeAdapter(Weapon.class, weaponDeserializer)
            .create();
    }

    /**
     * Loads game data from the specified path. This includes floor configurations,
     * enemy data, and weapon data for each floor.
     *
     * @param path the path to the main floor configuration file
     * @param towerHeight the height of the tower in levels
     * @throws IllegalArgumentException if the path is invalid or contains invalid data
     */
    public void loadGameData(final String path, final int towerHeight) {
        if (!verifyPath(path, towerHeight)) {
            throw new IllegalArgumentException("Invalid game data path");
        }
        this.floorsPath = path;
        loadFloors();
    }

    /**
     * Loads game data using the default tower height of 20 levels.
     *
     * @param path the path to the main floor configuration file
     * @throws IllegalArgumentException if the path is invalid or contains invalid data
     */
    public void loadGameData(final String path) {
        if (!verifyPath(path, DEFAULT_TOWER_HEIGHT)) {
            throw new IllegalArgumentException("Invalid game data path");
        }
        this.floorsPath = path;
        loadFloors();
    }

    private void loadFloors() {
        try (FileReader reader = new FileReader(floorsPath)) {
            final JsonArray floorsArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (final var floorElement : floorsArray) {
                final JsonObject floorObj = floorElement.getAsJsonObject();
                final String enemyPath = floorObj.get("enemyPath").getAsString();
                final String weaponsPath = floorObj.get("weaponsPath").getAsString();
                final String floorName = floorObj.get("floorName").getAsString();
                final String spritePath = floorObj.get("spritePath").getAsString();
                final int spawnWeight = floorObj.get("spawnWeight").getAsInt();
                final JsonObject spawnRange = floorObj.get("spawningRange").getAsJsonObject();
                final int minLevel = spawnRange.get("minLevel").getAsInt();
                final int maxLevel = spawnRange.get("maxLevel").getAsInt();
                final double visibility = floorObj.get("visibility").getAsDouble();

                final Optional<List<Enemy>> enemies = loadEnemies(enemyPath);
                final Optional<List<Weapon>> weapons = loadWeapons(weaponsPath);

                floors.add(new FloorData(
                    floorName,
                    spritePath,
                    enemies,
                    weapons,
                    new Pair<>(minLevel, maxLevel),
                    spawnWeight,
                    visibility
                ));
            }
        } catch (final IOException e) {
            LOGGER.error("Error loading floors: {}", e.getMessage(), e);
        }
    }

    private Optional<List<Enemy>> loadEnemies(final String enemyPath) {
        if (enemyPath.isEmpty()) {
            return Optional.empty();
        }
        try (FileReader reader = new FileReader(enemyPath)) {
            final List<Enemy> enemies = gson.fromJson(reader, new TypeToken<List<Enemy>>() { }.getType());
            return enemies != null && !enemies.isEmpty() ? Optional.of(enemies) : Optional.empty();
        } catch (final IOException e) {
            LOGGER.error("Error loading enemies: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    private Optional<List<Weapon>> loadWeapons(final String weaponsPath) {
        if (weaponsPath.isEmpty()) {
            return Optional.empty();
        }
        try (FileReader reader = new FileReader(weaponsPath)) {
            final List<Weapon> weapons = gson.fromJson(reader, new TypeToken<List<Weapon>>() { }.getType());
            return weapons != null && !weapons.isEmpty() ? Optional.of(weapons) : Optional.empty();
        } catch (final IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Returns a defensive copy of the loaded floor data.
     *
     * @return a new ArrayList containing all loaded floor configurations
     */
    public List<FloorData> getFloors() {
        return new ArrayList<>(floors);
    }

    /**
     * Verifies if the provided path contains valid game data configuration files.
     * Checks the existence and validity of the main floors file and its referenced enemy and weapon files.
     * 
     * @param testPath the path to verify
     * @param towerHeight the height of the tower in levels
     * @return true if all required files exist and are valid, false otherwise
     */
    public boolean verifyPath(final String testPath, final int towerHeight) {
        if (towerHeight < 1) {
            return false;
        }
        try (FileReader reader = new FileReader(Paths.get(testPath).toString())) {
            final JsonArray floorsArray = JsonParser.parseReader(reader).getAsJsonArray();
            if (floorsArray == null || floorsArray.size() == 0) {
                return false;
            }

            final JsonObject floor = floorsArray.get(0).getAsJsonObject();
            final String enemyPath = floor.get("enemyPath").getAsString();
            final String weaponsPath = floor.get("weaponsPath").getAsString();

            if (!verifyEnemyFile(enemyPath) || !verifyWeaponFile(weaponsPath)) {
                return false;
            }

            // Ensure that for each level 1 to towerHeight there is at least one floor covering it
            for (int level = 1; level <= towerHeight; level++) {
                boolean covered = false;
                for (final JsonElement floorElement : floorsArray) {
                    final JsonObject floorObj = floorElement.getAsJsonObject();
                    final JsonObject spawnRange = floorObj.get("spawningRange").getAsJsonObject();
                    final int minLevel = spawnRange.get("minLevel").getAsInt();
                    final int maxLevel = spawnRange.get("maxLevel").getAsInt();
                    if (level >= minLevel && level <= maxLevel) {
                        covered = true;
                        break;
                    }
                }
                if (!covered) {
                    return false;
                }
            }

            return true;
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies if the enemy configuration file exists and contains valid data.
     * 
     * @param enemyPath the path to the enemy configuration file
     * @return true if the file exists and contains valid enemy data, false otherwise
     */
    private boolean verifyEnemyFile(final String enemyPath) {
        try (FileReader reader = new FileReader(enemyPath)) {
            final JsonArray enemiesArray = JsonParser.parseReader(reader).getAsJsonArray();
            return enemiesArray != null && enemiesArray.size() > 0;
        } catch (final IOException e) {
            LOGGER.error("Error verifying enemy file: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Verifies if the weapons configuration file exists and contains valid data.
     * 
     * @param weaponsPath the path to the weapons configuration file
     * @return true if the file exists and contains valid weapon data, false otherwise
     */
    private boolean verifyWeaponFile(final String weaponsPath) {
        try (FileReader reader = new FileReader(weaponsPath)) {
            final JsonArray weaponsArray = JsonParser.parseReader(reader).getAsJsonArray();
            return weaponsArray != null && weaponsArray.size() > 0;
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
