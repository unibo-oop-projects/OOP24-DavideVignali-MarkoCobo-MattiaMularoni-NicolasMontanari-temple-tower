package it.unibo.templetower.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.unibo.templetower.model.EnemyRoom;
import it.unibo.templetower.model.Player;
import it.unibo.templetower.model.PlayerImpl;
import it.unibo.templetower.model.Room;
import it.unibo.templetower.model.Trap;
import it.unibo.templetower.model.Weapon;

public class GameControllerImpl implements GameController{
    Weapon weapon;;
    List<Room> rooms;
    Iterator<Room> roomsIt;
    Player player;

    public GameControllerImpl(){
        rooms = new ArrayList<>();
        rooms.add(new Room(new Trap(2), 1));
        rooms.add(new Room(new EnemyRoom(10.0, 3), 2));

        roomsIt = rooms.iterator();
        player = new PlayerImpl(weapon, rooms.getFirst());
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
        if (direction == 1){
            if(roomsIt.hasNext()){
                roomsIt.next().enter(player);
            }
        }else {
            //TODO fare iteratore doveè possibile anche andare indietro (next e previous)
        }
    }

    @Override
    public int getPlayerActualRoom() {
        return player.getActualRoom();
    }
}
