package it.unibo.templetower.model;

import java.util.List;

public class GameDataManagerImpl {
    private List<Floor> floors;
    private String floorsPath;

    public GameDataManagerImpl(String floorsPath){
        this.floorsPath=floorsPath;
    }

    public boolean verifyPath(){
        return false;
    }
}
