package it.unibo.templetower.controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import it.unibo.templetower.model.Floor;
import it.unibo.templetower.model.Player;
import it.unibo.templetower.model.PlayerImpl;
import it.unibo.templetower.model.Room;
import it.unibo.templetower.model.SpawnManagerImpl;
import it.unibo.templetower.model.Tower;
import it.unibo.templetower.model.Weapon;
import it.unibo.templetower.utils.AssetManager;
import it.unibo.templetower.utils.Pair;

/**
 * Implementation of the GameController interface that manages the game logic.
 * This class handles player movements, combat, and game state.
 */
public final class GameControllerImpl implements GameController {
    private List<Room> rooms;
    private int currentFloorIndex; // traccia il piano attuale
    private int currentRoomIndex;
    private final Player player;
    @SuppressWarnings("unused")
    private Floor currentFloor;
    private final AssetManager assetManager;
    private static final int PLAYERDIRECTION = 1;
    private static final int ENEMYDIRECTION = 0;
    private static final int ROOMS_NUMBER = 7;
    private static final String DEFAULT_TOWER_PATH = "tower/tower.json";
    private SpawnManagerImpl spawnManager;
    private boolean isBoss;

    /**
     * Constructs a new GameControllerImpl instance.
     * Initializes the game by setting up the floor and creating the player with an
     * initial weapon.
     */
    public GameControllerImpl() {
        currentFloorIndex = 1;
        isBoss = false;
        assetManager = new AssetManager();
        assetManager.addGenericEntityAsset("combat_view", "Images/enemy.png");
        assetManager.addGenericEntityAsset("treasure_view", "Images/treasure.png");
        assetManager.addGenericEntityAsset("trap_view", "Images/trap.png");
        assetManager.addGenericEntityAsset("stairs_view", "Images/stairs.png");
        assetManager.addGenericEntityAsset("empty_view", "Images/smoke.gif");
        final Weapon startWeapon = new Weapon("GUN", 1, new Pair<>("Gun", 1.0), DEFAULT_TOWER_PATH);
        // Initialize player
        player = new PlayerImpl(startWeapon, Optional.empty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void goToNextFloor() {
        currentFloorIndex += 1;
        currentRoomIndex = 0;
        rooms.clear();
        currentFloor = spawnManager.spawnFloor(currentFloorIndex, ROOMS_NUMBER);
        rooms.addAll(currentFloor.rooms());

        if ("boss_view".equals(rooms.get(0).getName())) {
            isBoss = true;
        }
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

    @Override
    public void increaseLifePlayer(final int xp) {
        player.increaseExperience(xp);
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
        return ROOMS_NUMBER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetGame() {
        isBoss = false;
        final GameDataManagerImpl gameDataManager = GameDataManagerImpl.getInstance();
        gameDataManager.loadGameDataFromTower(gameDataManager.getTowerPath().get());
        final Tower towerData = gameDataManager.getTower();
        spawnManager = new SpawnManagerImpl(towerData);
        final Floor generatedFloor = spawnManager.spawnFloor(1, ROOMS_NUMBER); // Assuming 7 rooms per floor
        currentFloor = generatedFloor;
        rooms = generatedFloor.rooms();
        currentRoomIndex = 0;
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
        player.takeDamage(this.rooms.get(currentRoomIndex).getTrapDamage());
    }

    @Override
    public void removeWeapon(final int index) {
        player.addWeapon(rooms.get(currentRoomIndex).getWeapon(), index);
    }

    @Override
    public int getElementTreasure() {
        this.rooms.get(currentRoomIndex).interactWithRoom(player, ENEMYDIRECTION);
        return this.rooms.get(currentRoomIndex).getElementTreasure();
    }

    @Override
    public Weapon getTreasureWeapon() {
        return this.rooms.get(currentRoomIndex).getWeapon();
    }

    @Override
    public String getRoomImagePath(final int index) {
        return assetManager.getGenericEntityAsset(rooms.get(index).getName());
    }

    @Override
    public Boolean isRoomToDisplay() {
        final double roll = ThreadLocalRandom.current().nextDouble(1);
        return roll >= this.currentFloor.visibility();
    }

    @Override
    public int getXpTreasure() {
        return this.rooms.get(currentRoomIndex).getXP();
    }

    @Override
    public Boolean isBossTime() {
        return isBoss;
    }
}
