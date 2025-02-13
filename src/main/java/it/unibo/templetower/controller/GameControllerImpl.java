package it.unibo.templetower.controller;

import java.util.List;
import java.util.Optional;

import it.unibo.templetower.model.Floor;
import it.unibo.templetower.model.FloorData;
import it.unibo.templetower.model.Player;
import it.unibo.templetower.model.PlayerImpl;
import it.unibo.templetower.model.Room;
import it.unibo.templetower.model.SpawnManagerImpl;
import it.unibo.templetower.model.Weapon;
import it.unibo.templetower.utils.AssetManager;
import it.unibo.templetower.utils.Pair;

/**
 * Implementation of the GameController interface that manages the game logic.
 * This class handles player movements, combat, and game state.
 */
public final class GameControllerImpl implements GameController {
    private final List<Room> rooms;
    private int currentRoomIndex; // Traccia la stanza attuale
    private int currentFloorIndex; // traccia il piano attuale
    private final Player player;
    @SuppressWarnings("unused")
    private Floor currentFloor;
    private final GameDataManagerImpl gameDataManager;
    private final AssetManager assetManager;
    private static final int PLAYERDIRECTION = 1;
    private static final int ENEMYDIRECTION = 0;
    private static final int ROOMS_NUMBER = 8;
    private static final int DEFAULT_ENEMY_LEVEL = 12;
    private static final Double DAMAGE = 80.0;
    private final Weapon startWeapon;
    private final SpawnManagerImpl spawnManager;

    /**
     * Constructs a new GameControllerImpl instance.
     * Initializes the game by loading data, setting up the floor,
     * and creating the player with initial weapon.
     */
    public GameControllerImpl() {
        // Load game data
        gameDataManager = new GameDataManagerImpl();
        final String testPath = "tower/floors/floors-data.json";
        gameDataManager.loadGameData(testPath);
        final List<FloorData> floors = gameDataManager.getFloors();
        currentFloorIndex = 1;
        currentRoomIndex = 1;

        // Instantiate SpawnManagerImpl with loaded floor data
        spawnManager = new SpawnManagerImpl(floors);

        currentFloor = spawnManager.spawnFloor(currentFloorIndex, ROOMS_NUMBER);

        /* test asset manager */
        assetManager = new AssetManager();
        assetManager.addEnemyAsset(DEFAULT_ENEMY_LEVEL, "images/enemy.png");

        // first weapon
        startWeapon = new Weapon("GUN", 1, new Pair<>("Gun", DAMAGE), testPath);

        rooms = currentFloor.rooms();
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
        currentFloorIndex += 1;
        rooms.clear();
        currentFloor = spawnManager.spawnFloor(currentFloorIndex, ROOMS_NUMBER);
        rooms.addAll(currentFloor.rooms());
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
            currentRoomIndex = rooms.size() - 1; // Torna all'ultima stanza
        } else {
            currentRoomIndex = 0; // Torna alla prima stanza
        }
    }

    /**
     * Retrieves the current player.
     * 
     * @return the player
     */
    @Override
    public List<Weapon> getPlayerWeapons() {
        return player.getAllWeapons();
    }

    @Override
    public void addPlayerWeapon(final Weapon newWeapon, final int index) {
        player.addWeapon(newWeapon, index);
    }

    @Override
    public void changeWeaponIndex(final int index) {
        player.changeWeapon(index);
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
    public String getEnemySpritePath() {
        return this.rooms.get(currentRoomIndex).getEnemySprite();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEntiSpritePath(final String type) {
        return assetManager.getGenericEntityAsset(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEnemySpritePath(final int level) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnemySpritePath'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gameOver() {
        final GameDataManagerImpl gameDataManager = new GameDataManagerImpl();
        final String testPath = "tower/floors/floors-data.json";
        gameDataManager.loadGameData(testPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetPlayerLife() {
        player.resetLife();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerTakeDamage() {
        player.takeDamage(rooms.get(currentRoomIndex).getTrapDamage());
    }

    @Override
    public void removeWeapon(final int index) {
        //player.addWeapon(rooms.get(currentRoomIndex).getWeapon(), index);
    }

    @Override
    public int getElementTreasure() {
        return this.rooms.get(currentRoomIndex).getElementTreasure();
    }

    @Override
    public Weapon getTreasureWeapon() {
        return this.rooms.get(currentRoomIndex).getWeapon();
    }
}
