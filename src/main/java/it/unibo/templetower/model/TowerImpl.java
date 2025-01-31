package it.unibo.templetower.model;

public class TowerImpl implements Tower{
    private int currentFloorIndex = 0;
    private PlayerImpl player;

    public TowerImpl(PlayerImpl player) {
        this.player = player;
    }

    @Override
    public void generateLevels() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateLevels'");
    }

    @Override
    public RoomBehavior getCurrentRoom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentRoom'");
    }

    /**
     * Moves the player to the specified floor index within the list of floors.
     *
     * <p>This method updates the current floor index and retrieves the 
     * corresponding floor from the list of floors. If the provided floor index 
     * is invalid (negative or out of bounds), an {@link IllegalArgumentException} 
     * is thrown.</p>
     *
     * @param floorIndex the index of the floor to move the player to
     *                   (must be between 0 and {@code floors.size() - 1}).
     * @throws IllegalArgumentException if the specified {@code floorIndex} is 
     *                                  not within the valid range
     */
    @Override
    public void movePlayer(int floorIndex) {
        /* if (floorIndex >= 0 && floorIndex < floors.size()) {
            currentFloorIndex = floorIndex;
        } else {
            throw new IllegalArgumentException("Invalid floor index");
        } */
    }
    
}
