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

public class GameControllerImpl implements GameController {
    private final List<Room> rooms;
    private int currentRoomIndex; // Traccia la stanza attuale
    private int currentFloorIndex; // traccia il piano attuale
    private Floor currentFloor;
    private final Player player;
    private final GameDataManagerImpl gameDataManager;
    private final AssetManager assetManager;
    private static final int PLAYERDIRECTION = 1;
    private static final int ENEMYDIRECTION = 0;
    private static final int ROOMS_NUMBER = 8;
    private final Weapon startWeapon;
    private SpawnManagerImpl spawnManager;

    public GameControllerImpl() {
        // Load game data
        gameDataManager = new GameDataManagerImpl();
        String testPath = "tower/floors/floors-data.json";
        gameDataManager.loadGameData(testPath);
        List<FloorData> floors = gameDataManager.getFloors();
        currentFloorIndex = 1;
        currentRoomIndex = 1;

        // Instantiate SpawnManagerImpl with loaded floor data
        spawnManager = new SpawnManagerImpl(floors);

        currentFloor = spawnManager.spawnFloor(currentFloorIndex, ROOMS_NUMBER);

        /* test asset manager */
        assetManager = new AssetManager();
        assetManager.addEnemyAsset(12, "images/enemy.png");

        // first weapon
        startWeapon = new Weapon("GUN", 1, new Pair<String, Double>("Gun", 30.0), testPath);

        rooms = currentFloor.rooms();
        player = new PlayerImpl(startWeapon, Optional.empty());
    }

    @Override
    public void startGame() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void endGame() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleAction(String action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

    @Override
    public void goToNextFloor() {
        currentFloorIndex += 1;
        rooms.removeAll(rooms);
        currentFloor = spawnManager.spawnFloor(currentFloorIndex, ROOMS_NUMBER);
        rooms.addAll(currentFloor.rooms());
    }

    @Override
    public void changeRoom(Integer direction) {
        int newIndex = currentRoomIndex + direction;

        if (newIndex >= 0 && newIndex < rooms.size()) {
            currentRoomIndex = newIndex;
        } else if (newIndex < 0) {
            currentRoomIndex = rooms.size() - 1; // Torna all'ultima stanza
        } else {
            currentRoomIndex = 0; // Torna alla prima stanza
        }
    }

    @Override
    public void attackEnemy() {
        rooms.get(currentRoomIndex).interactWithRoom(player, ENEMYDIRECTION);
    }

    @Override
    public void attackPlayer() {
        rooms.get(currentRoomIndex).interactWithRoom(player, PLAYERDIRECTION);
    }

    @Override
    public double getPlayerLife() {
        rooms.get(currentRoomIndex).getLifePlayer(player);
        return player.getLife();
    }

    @Override
    public double getEnemyLifePoints() {
        System.out.println(rooms.get(currentRoomIndex).getEnemyLife());
        return rooms.get(currentRoomIndex).getEnemyLife();
    }

    @Override
    public String enterRoom() {
        rooms.get(currentRoomIndex).enter(player);
        return rooms.get(currentRoomIndex).getName();
    }

    @Override
    public int getPlayerActualRoom() {
        return currentRoomIndex;
    }

    @Override
    public int getNumberOfRooms() {
        return rooms.size();
    }

    @Override
    public String getEnemySpritePath() {
        return this.rooms.get(currentRoomIndex).getEnemySprite();
    }

    @Override
    public String getEntiSpritePath(String type) {
        return assetManager.getGenericEntityAsset(type);
    }
}
