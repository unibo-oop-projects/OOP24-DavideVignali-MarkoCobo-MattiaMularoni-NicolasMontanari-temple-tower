package it.unibo.templetower.model;

import java.util.ArrayList;
import java.util.List;

public class FloorImpl implements Floor {

    private int id;
    private List<Room> room;
    private int indexRoom;

    public FloorImpl(int id) {
        this.id = id;
        this.room = new ArrayList<>();
    }

    public int getId(){
        return this.id;
    }
    
    @Override
    public void generateRooms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateRooms'");
    }
    
}
