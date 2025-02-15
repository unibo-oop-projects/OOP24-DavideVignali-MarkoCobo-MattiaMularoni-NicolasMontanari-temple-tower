package it.unibo.templetower.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a trap that can damage players when they interact with it.
 */
public final class Trap implements RoomBehavior {
    private static final Logger LOGGER = LoggerFactory.getLogger(Trap.class);
    private final int damage;

    /**
     * Constructs a new trap with specified damage.
     * @param damage the amount of damage this trap deals
     */
    public Trap(final int damage) {
        this.damage = damage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateContent() {
        // No content needs to be generated for traps
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void interact(final Player player, final int direction) {
        LOGGER.info("Player triggered a trap");
        player.takeDamage(damage);
    }

    /**
     * Gets the damage amount of this trap.
     * @return the damage amount
     */
    public int getDamage() {
        return this.damage;
    }

    @Override
    public int getElement() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getElement'");
    }
}
