package it.unibo.templetower.model;

/**
 * Represents a trap that can damage players when they interact with it.
 */
public final class Trap implements RoomBehavior {
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
        System.out.println("Player take a trap");
        player.takeDamage(damage);
    }

    /**
     * Gets the damage amount of this trap.
     * @return the damage amount
     */
    public int getDamage() {
        return this.damage;
    }
}
