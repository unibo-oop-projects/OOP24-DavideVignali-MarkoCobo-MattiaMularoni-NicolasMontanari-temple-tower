package it.unibo.templetower.model;

public class TreasureRoom implements RoomBehavior{
    private final int xps;
    public TreasureRoom(final int xps){
        this.xps = xps;
    }

    @Override
    public void interact(Player player) {
        player.increaseExperience(this.xps);
    }

    @Override
    public void generateContent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateContent'");
    }
    
}
