package it.unibo.templetower.model;

/**
 * Represents a room with stairs in the temple tower.
 * This room allows players to move between different levels of the tower.
 */
public final class StairsRoom implements RoomBehavior {

    @Override
    public void interact(final Player player, final int direction) {
        // Empty implementation as interaction is handled elsewhere
    }

}
