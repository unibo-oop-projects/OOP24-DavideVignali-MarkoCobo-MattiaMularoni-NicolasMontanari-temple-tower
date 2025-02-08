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

public class GameControllerImpl implements GameController{
    private Weapon weapon;
    private final List<Room> rooms;
    private int currentRoomIndex = 0; // Traccia la stanza attuale
    private final Player player;
    private final GameDataManagerImpl gameDataManager;
    private final AssetManager assetManager;
    private static final int ROOMS_NUMBER = 8;

    public GameControllerImpl(){
        // Load game data
        gameDataManager = new GameDataManagerImpl();
        String testPath = "tower/floors/floors-data.json";
        gameDataManager.loadGameData(testPath);
        List<FloorData> floors = gameDataManager.getFloors();

        // Instantiate SpawnManagerImpl with loaded floor data
        SpawnManagerImpl spawnManager = new SpawnManagerImpl(floors);

        //TODO al posto dell'1 implementare logica di cambio piano
        Floor generatedFloor = spawnManager.spawnFloor(1, ROOMS_NUMBER);

        /* test asset manager */
        assetManager = new AssetManager();
        assetManager.addEnemyAsset(12, "images/enemy.png");

        rooms = generatedFloor.rooms();
        player = new PlayerImpl(weapon, Optional.empty());
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
        throw new UnsupportedOperationException("Not supported yet.");
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
    public String getEnemySpritePath(int level){
        return assetManager.getEnemyAsset(level);
    }

    @Override
    public String getEntiSpritePath(String type) {
        return assetManager.getGenericEntityAsset(type);
    }
}
