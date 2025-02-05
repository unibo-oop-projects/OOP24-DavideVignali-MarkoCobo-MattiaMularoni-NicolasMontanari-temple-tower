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

/**
 * Implementation of the Game Data Manager that handles loading and verification of game data from JSON files.
 * This class is responsible for loading and managing floor configurations, including their associated
 * enemies and weapons data from JSON configuration files.
 */
public class GameDataManagerImpl {
    private List<FloorData> floors;
    private String floorsPath;
    private final Gson gson;

    /**
     * Creates a new GameDataManagerImpl instance with an empty floors list and configured Gson instance.
     * The Gson instance is configured with custom deserializers for Enemy and Weapon classes to properly
     * handle the attack data structures from JSON.
     */
    public GameDataManagerImpl() {
        this.floors = new ArrayList<>();
        
        // Create custom deserializer for Enemy class
        JsonDeserializer<Enemy> enemyDeserializer = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            Double health = jsonObject.get("health").getAsDouble();
            int level = jsonObject.get("level").getAsInt();
            String spritePath = jsonObject.get("spritePath").getAsString();
            
            List<Pair<String, Double>> attacks = new ArrayList<>();
            JsonArray attacksArray = jsonObject.getAsJsonArray("attacks");
            for (JsonElement attackElement : attacksArray) {
                JsonObject attackObj = attackElement.getAsJsonObject();
                String attackId = attackObj.get("attackId").getAsString();
                Double damage = attackObj.get("damage").getAsDouble();
                attacks.add(new Pair<>(attackId, damage));
            }

            Map<String, Double> multipliersMap = new HashMap<>();
            JsonArray multipliersArray = jsonObject.getAsJsonArray("damageMultipliers");
            if (multipliersArray != null) {
                for (JsonElement multiplierElement : multipliersArray) {
                    JsonObject multiplierObj = multiplierElement.getAsJsonObject();
                    String attackId = multiplierObj.get("attackId").getAsString();
                    Double multiplier = multiplierObj.get("multiplier").getAsDouble();
                    multipliersMap.put(attackId, multiplier);
                }
            }
            
            return new Enemy(name, health, level, attacks, multipliersMap, spritePath);
        };

        // Custom deserializer for Weapon class
        JsonDeserializer<Weapon> weaponDeserializer = (json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            Integer level = jsonObject.get("level").getAsInt();
            String spritePath = jsonObject.get("spritePath").getAsString();
            
            JsonArray attacksArray = jsonObject.getAsJsonArray("attacks");
            JsonObject attackObj = attacksArray.get(0).getAsJsonObject();
            String attackId = attackObj.get("attackId").getAsString();
            Double damage = attackObj.get("damage").getAsDouble();
            
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
     * @throws IllegalArgumentException if the path is invalid or contains invalid data
     */
    public void loadGameData(String path, int towerHeight) {
        if (!verifyPath(path, towerHeight)) {
            throw new IllegalArgumentException("Invalid game data path");
        }
        this.floorsPath = path;
        loadFloors();
    }
    public void loadGameData(String path) {
        if (!verifyPath(path, 20)) {
            throw new IllegalArgumentException("Invalid game data path");
        }
        this.floorsPath = path;
        loadFloors();
    }

    private void loadFloors() {
        try (FileReader reader = new FileReader(floorsPath)) {
            JsonArray floorsArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (var floorElement : floorsArray) {
                JsonObject floorObj = floorElement.getAsJsonObject();
                String enemyPath = floorObj.get("enemyPath").getAsString();
                String weaponsPath = floorObj.get("weaponsPath").getAsString();
                String floorName = floorObj.get("floorName").getAsString();
                String spritePath = floorObj.get("spritePath").getAsString();
                int spawnWeight = floorObj.get("spawnWeight").getAsInt();
                JsonObject spawnRange = floorObj.get("spawningRange").getAsJsonObject();
                int minLevel = spawnRange.get("minLevel").getAsInt();
                int maxLevel = spawnRange.get("maxLevel").getAsInt();
                double visibility = floorObj.get("visibility").getAsDouble();

                Optional<List<Enemy>> enemies = loadEnemies(enemyPath);
                Optional<List<Weapon>> weapons = loadWeapons(weaponsPath);

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
        } catch (IOException e) {
            throw new RuntimeException("Error loading floor data", e);
        }
    }

    private Optional<List<Enemy>> loadEnemies(String enemyPath) {
        if (enemyPath.isEmpty()) {
            return Optional.empty();
        }
        try (FileReader reader = new FileReader(enemyPath)) {
            List<Enemy> enemies = gson.fromJson(reader, new TypeToken<List<Enemy>>(){}.getType());
            return enemies != null && !enemies.isEmpty() ? Optional.of(enemies) : Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private Optional<List<Weapon>> loadWeapons(String weaponsPath) {
        if (weaponsPath.isEmpty()) {
            return Optional.empty();
        }
        try (FileReader reader = new FileReader(weaponsPath)) {
            List<Weapon> weapons = gson.fromJson(reader, new TypeToken<List<Weapon>>(){}.getType());
            return weapons != null && !weapons.isEmpty() ? Optional.of(weapons) : Optional.empty();
        } catch (IOException e) {
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
    public boolean verifyPath(String testPath, int towerHeight){
        if(towerHeight < 1){
            return false;
        }
        testPath = Paths.get(testPath).toString();
        try (FileReader reader = new FileReader(testPath)) {
            JsonArray floorsArray = JsonParser.parseReader(reader).getAsJsonArray();
            if (floorsArray == null || floorsArray.size() == 0) {
                return false;
            }

            JsonObject floor = floorsArray.get(0).getAsJsonObject();
            String enemyPath = floor.get("enemyPath").getAsString();
            String weaponsPath = floor.get("weaponsPath").getAsString();

            if (!verifyEnemyFile(enemyPath) || !verifyWeaponFile(weaponsPath)) {
                return false;
            }

            // Ensure that for each level 1 to towerHeight there is at least one floor covering it
            for (int level = 1; level <= towerHeight; level++){
                boolean covered = false;
                for (JsonElement floorElement : floorsArray) {
                    JsonObject floorObj = floorElement.getAsJsonObject();
                    JsonObject spawnRange = floorObj.get("spawningRange").getAsJsonObject();
                    int minLevel = spawnRange.get("minLevel").getAsInt();
                    int maxLevel = spawnRange.get("maxLevel").getAsInt();
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
        } catch (IOException e) {
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
    private boolean verifyEnemyFile(String enemyPath) {
        try (FileReader reader = new FileReader(enemyPath)) {
            JsonArray enemiesArray = JsonParser.parseReader(reader).getAsJsonArray();
            return enemiesArray != null && enemiesArray.size() > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies if the weapons configuration file exists and contains valid data.
     * 
     * @param weaponsPath the path to the weapons configuration file
     * @return true if the file exists and contains valid weapon data, false otherwise
     */
    private boolean verifyWeaponFile(String weaponsPath) {
        try (FileReader reader = new FileReader(weaponsPath)) {
            JsonArray weaponsArray = JsonParser.parseReader(reader).getAsJsonArray();
            return weaponsArray != null && weaponsArray.size() > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
