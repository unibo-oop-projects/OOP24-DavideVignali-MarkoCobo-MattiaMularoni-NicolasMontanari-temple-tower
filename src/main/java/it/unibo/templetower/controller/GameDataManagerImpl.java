package it.unibo.templetower.controller;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.File;

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
import it.unibo.templetower.model.Tower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Game Data Manager that handles loading and verification of game data from JSON files.
 * This class is responsible for loading and managing floor configurations, including their associated
 * enemies and weapons data from JSON configuration files.
 */
public final class GameDataManagerImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameDataManagerImpl.class);
    private static final String ATTACK_ID_KEY = "attackId";
    private static final String NAME_KEY = "name";
    private final List<FloorData> floors;
    private String floorsPath;
    private final Gson gson;
    private String baseDir;
    private Tower towerData;
    private String selectedTowerPath;

    private GameDataManagerImpl() {
        this.floors = new ArrayList<>();

        // Create custom deserializer for Enemy class
        final JsonDeserializer<Enemy> enemyDeserializer = (json, typeOfT, context) -> {
            final JsonObject jsonObject = json.getAsJsonObject();
            final String name = jsonObject.get(NAME_KEY).getAsString();
            final Double health = jsonObject.get("health").getAsDouble();
            final int level = jsonObject.get("level").getAsInt();
            final String spritePath = jsonObject.get("spritePath").getAsString();

            final List<Pair<String, Double>> attacks = new ArrayList<>();
            final JsonArray attacksArray = jsonObject.getAsJsonArray("attacks");
            for (final JsonElement attackElement : attacksArray) {
                final JsonObject attackObj = attackElement.getAsJsonObject();
                final String attackId = attackObj.get(ATTACK_ID_KEY).getAsString();
                final Double damage = attackObj.get("damage").getAsDouble();
                attacks.add(new Pair<>(attackId, damage));
            }

            final Map<String, Double> multipliersMap = new HashMap<>();
            final JsonArray multipliersArray = jsonObject.getAsJsonArray("damageMultipliers");
            if (multipliersArray != null) {
                for (final JsonElement multiplierElement : multipliersArray) {
                    final JsonObject multiplierObj = multiplierElement.getAsJsonObject();
                    final String attackId = multiplierObj.get(ATTACK_ID_KEY).getAsString();
                    final Double multiplier = multiplierObj.get("multiplier").getAsDouble();
                    multipliersMap.put(attackId, multiplier);
                }
            }

            return new Enemy(name, health, level, attacks, multipliersMap, spritePath);
        };

        // Custom deserializer for Weapon class
        final JsonDeserializer<Weapon> weaponDeserializer = (json, typeOfT, context) -> {
            final JsonObject jsonObject = json.getAsJsonObject();
            final String name = jsonObject.get(NAME_KEY).getAsString();
            final Integer level = jsonObject.get("level").getAsInt();
            final String spritePath = jsonObject.get("spritePath").getAsString();

            final JsonArray attacksArray = jsonObject.getAsJsonArray("attacks");
            final JsonObject attackObj = attacksArray.get(0).getAsJsonObject();
            final String attackId = attackObj.get(ATTACK_ID_KEY).getAsString();
            final Double damage = attackObj.get("damage").getAsDouble();

            return new Weapon(name, level, new Pair<>(attackId, damage), spritePath);
        };

        // Configure Gson with custom deserializers
        this.gson = new GsonBuilder()
            .registerTypeAdapter(Enemy.class, enemyDeserializer)
            .registerTypeAdapter(Weapon.class, weaponDeserializer)
            .create();
    }

    private static final class InstanceHolder {
        private static final GameDataManagerImpl INSTANCE = new GameDataManagerImpl();
    }

    /**
     * Gets the singleton instance of GameDataManagerImpl.
     * Using initialization-on-demand holder idiom for thread-safe lazy initialization
     * @return the singleton instance
     */
    public static GameDataManagerImpl getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Loads game data from the specified path. This includes floor configurations,
     * enemy data, and weapon data for each floor.
     *
     * @param path the path to the main floor configuration file
     * @throws IllegalArgumentException if the path is invalid or contains invalid data
     */
    public void loadGameData(final String path) {
        if (!verifyPath(path)) {
            throw new IllegalArgumentException("Invalid game data path");
        }
        this.floorsPath = path;
        loadFloors();
    }

    /**
     * Loads game data from the tower configuration file.
     *
     * @param towerJsonPath the path to the tower configuration file
     */
    public void loadGameDataFromTower(final String towerJsonPath) {
        final File towerFile = new File(towerJsonPath);
        this.baseDir = towerFile.getParent();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(towerJsonPath), StandardCharsets.UTF_8)) {
            final JsonObject towerObj = JsonParser.parseReader(reader).getAsJsonObject();
            // Validate height field exists and is valid
            if (!towerObj.has("height") || towerObj.get("height").getAsInt() <= 0) {
                LOGGER.error("Invalid or missing tower height in configuration");
                throw new IllegalArgumentException("Invalid or missing tower height in configuration");
            }
            final String relativeFloorsPath = towerObj.get("pathToFloors").getAsString();
            final String absFloorsPath = Paths.get(baseDir, relativeFloorsPath).toString();
            final String relativeAttacksPath = towerObj.get("pathToAttacks").getAsString();
            final String absAttacksPath = Paths.get(baseDir, relativeAttacksPath).toString();
            final int height = towerObj.get("height").getAsInt();
            this.towerData = new Tower(
                towerObj.get("name").getAsString(),
                towerObj.get("description").getAsString(),
                new ArrayList<>(),
                new HashMap<>(),
                height
            );
            loadGameData(absFloorsPath);
            final Map<String, String> attacksSprite = loadAttacksData(absAttacksPath);
            this.towerData = new Tower(
                this.towerData.name(),
                this.towerData.description(),
                new ArrayList<>(this.floors),
                attacksSprite,
                this.towerData.height()
            );
        } catch (final IOException e) {
            final String message = "Error loading tower file: " + e.getMessage();
            LOGGER.error(message, e);
            throw new IllegalArgumentException(message, e);
        }
    }

    // NEW: Helper method to load attacks data from JSON file
    private Map<String, String> loadAttacksData(final String attacksPath) {
        final Map<String, String> attacksMap = new HashMap<>();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(attacksPath), StandardCharsets.UTF_8)) {
            final JsonArray attacksArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (final JsonElement element : attacksArray) {
                final JsonObject obj = element.getAsJsonObject();
                final String id = obj.get(ATTACK_ID_KEY).getAsString();
                final String effect = obj.get("effectPath").getAsString();
                // Maintain relative paths (baseDir is tower folder)
                attacksMap.put(id, effect);
            }
        } catch (final IOException e) {
            LOGGER.error("Error loading attacks data: {}", e.getMessage(), e);
        }
        return attacksMap;
    }

    /**
     * Loads and returns basic tower information from a tower.json file.
     * 
     * @param towerJsonPath the path to the tower.json file
     * @return a Pair containing the tower name and description
     * @throws IOException if there's an error reading the file
     */
    public Pair<String, String> loadTowerInfo(final String towerJsonPath) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(towerJsonPath), StandardCharsets.UTF_8)) {
            final JsonObject towerObj = JsonParser.parseReader(reader).getAsJsonObject();
            final String name = towerObj.get(NAME_KEY).getAsString();
            final String description = towerObj.get("description").getAsString();
            return new Pair<>(name, description);
        }
    }

    /**
     * Returns the tower configuration. If not loaded, loads it from the selected path.
     *
     * @return the tower record containing name, description, floors and attacks sprite data.
     * @throws IllegalStateException if no tower path is selected
     */
    public Tower getTower() {
        if (this.towerData == null) {
            final String path = this.selectedTowerPath;
            if (path == null) {
                throw new IllegalStateException("No tower selected");
            }
            loadGameDataFromTower(path);
        }
        return this.towerData;
    }

    /**
     * Sets the path to the currently selected tower for gameplay.
     * This saves memory by not loading the tower until needed.
     *
     * @param towerPath the path to the tower.json file
     */
    public void setTowerPath(final String towerPath) {
        this.selectedTowerPath = towerPath;
        this.towerData = null;
    }

    /**
     * Gets the path to the currently selected tower for gameplay.
     *
     * @return Optional containing the selected tower path, or empty if none is selected
     */
    public Optional<String> getTowerPath() {
        return Optional.ofNullable(this.selectedTowerPath);
    }

    private void loadFloors() {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(floorsPath), StandardCharsets.UTF_8)) {
            final JsonArray floorsArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (final var floorElement : floorsArray) {
                final JsonObject floorObj = floorElement.getAsJsonObject();
                final String enemyPathRel = floorObj.get("enemyPath").getAsString();
                final String enemyPath = Paths.get(baseDir, enemyPathRel).toString();
                final String weaponsPathRel = floorObj.get("weaponsPath").getAsString();
                final String weaponsPath = Paths.get(baseDir, weaponsPathRel).toString();
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
            LOGGER.error("Error loading floors: {}", e.getMessage());
            throw new IllegalStateException("Failed to load floors", e);
        }
    }

    private Optional<List<Enemy>> loadEnemies(final String enemyPath) {
        if (enemyPath.isEmpty()) {
            return Optional.empty();
        }
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(enemyPath), StandardCharsets.UTF_8)) {
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
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(weaponsPath), StandardCharsets.UTF_8)) {
            final List<Weapon> weapons = gson.fromJson(reader, new TypeToken<List<Weapon>>() { }.getType());
            return weapons != null && !weapons.isEmpty() ? Optional.of(weapons) : Optional.empty();
        } catch (final IOException e) {
            LOGGER.error("Error loading weapons: {}", e.getMessage(), e);
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
     * @return true if all required files exist and are valid, false otherwise
     */
    public boolean verifyPath(final String testPath) {
        if (this.towerData == null || this.towerData.height() < 1) {
            return false;
        }
        try (InputStreamReader reader = new InputStreamReader(
            new FileInputStream(Paths.get(testPath).toString()), StandardCharsets.UTF_8)) {
            final JsonArray floorsArray = JsonParser.parseReader(reader).getAsJsonArray();
            if (floorsArray == null || floorsArray.size() == 0) {
                return false;
            }

            final JsonObject floor = floorsArray.get(0).getAsJsonObject();
            final String enemyPathRel = floor.get("enemyPath").getAsString();
            final String enemyPath = Paths.get(baseDir, enemyPathRel).toString();
            final String weaponsPathRel = floor.get("weaponsPath").getAsString();
            final String weaponsPath = Paths.get(baseDir, weaponsPathRel).toString();

            if (!verifyEnemyFile(enemyPath) || !verifyWeaponFile(weaponsPath)) {
                return false;
            }

            // Ensure that for each level 1 to towerHeight there is at least one floor covering it
            for (int level = 1; level <= this.towerData.height(); level++) {
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
            LOGGER.error("Error verifying path: {}", e.getMessage(), e);
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
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(enemyPath), StandardCharsets.UTF_8)) {
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
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(weaponsPath), StandardCharsets.UTF_8)) {
            final JsonArray weaponsArray = JsonParser.parseReader(reader).getAsJsonArray();
            return weaponsArray != null && weaponsArray.size() > 0;
        } catch (final IOException e) {
            LOGGER.error("Error verifying weapon file: {}", e.getMessage(), e);
            return false;
        }
    }
}
