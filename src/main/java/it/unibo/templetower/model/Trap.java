package it.unibo.templetower.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a trap that can damage players when they interact with it.
 */
public final class Trap implements RoomBehavior {
    private static final Logger LOGGER = LoggerFactory.getLogger(Trap.class);
    private final double damage;

    /**
     * Constructs a new trap with specified damage.
     * @param damage the amount of damage this trap deals
     */
    public Trap(final double damage) {
        this.damage = damage;
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
    public double getDamage() {
        return this.damage;
    }
}
