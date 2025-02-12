package it.unibo.templetower.controller;

import java.util.List;
import java.util.Optional;
import it.unibo.templetower.model.Floor;
import it.unibo.templetower.model.Player;
import it.unibo.templetower.model.PlayerImpl;
import it.unibo.templetower.model.Room;
import it.unibo.templetower.model.SpawnManagerImpl;
import it.unibo.templetower.model.Weapon;
import it.unibo.templetower.utils.AssetManager;
import it.unibo.templetower.utils.Pair;
import it.unibo.templetower.model.Tower;

/**
 * Implementation of the GameController interface that manages the game logic.
 * This class handles player movements, combat, and game state.
 */
public final class GameControllerImpl implements GameController {
    private final List<Room> rooms;
    private int currentRoomIndex;
    private final Player player;
    private final GameDataManagerImpl gameDataManager;
    private final AssetManager assetManager;
    private static final int PLAYERDIRECTION = 1;
    private static final int ENEMYDIRECTION = 0;
    private static final int ROOMS_NUMBER = 8;
    private static final int DEFAULT_ENEMY_LEVEL = 12;
    private final Weapon startWeapon;

    /**
     * Constructs a new GameControllerImpl instance.
     * Initializes the game by loading tower data, setting up the floor,
     * and creating the player with an initial weapon.
     */
    public GameControllerImpl() {
        gameDataManager = new GameDataManagerImpl();
        final String towerPath = "tower/tower.json";
        gameDataManager.loadGameDataFromTower(towerPath);
        final Tower towerData = gameDataManager.getTower();
        final SpawnManagerImpl spawnManager = new SpawnManagerImpl(towerData);
        final Floor generatedFloor = spawnManager.spawnFloor(1, ROOMS_NUMBER);
        assetManager = new AssetManager();
        assetManager.addEnemyAsset(DEFAULT_ENEMY_LEVEL, "images/enemy.png");
        startWeapon = new Weapon("GUN", 1, new Pair<>("Gun", 1.0), "tower/tower.json");
        rooms = generatedFloor.rooms();
        player = new PlayerImpl(startWeapon, Optional.empty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleAction(final String action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void goToNextFloor() {
        // TODO implements logic to change floor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeRoom(final Integer direction) {
        final int newIndex = currentRoomIndex + direction;
        if (newIndex >= 0 && newIndex < rooms.size()) {
            currentRoomIndex = newIndex;
        } else if (newIndex < 0) {
            currentRoomIndex = rooms.size() - 1;
        } else {
            currentRoomIndex = 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attackEnemy() {
        rooms.get(currentRoomIndex).interactWithRoom(player, ENEMYDIRECTION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attackPlayer() {
        rooms.get(currentRoomIndex).interactWithRoom(player, PLAYERDIRECTION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getPlayerLife() {
        return player.getLife();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getEnemyLifePoints() {
        return rooms.get(currentRoomIndex).getEnemyLife();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String enterRoom() {
        rooms.get(currentRoomIndex).enter(player);
        return rooms.get(currentRoomIndex).getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerActualRoom() {
        return currentRoomIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfRooms() {
        return rooms.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEnemySpritePath(final int level) {
        return assetManager.getEnemyAsset(level);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEntiSpritePath(final String type) {
        return assetManager.getGenericEntityAsset(type);
    }
}
