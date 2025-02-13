package it.unibo.templetower.model;

import java.util.Random;

/**
 * Represents a room in the game that contains an enemy.
 * The enemy room contains an enemy that the player must defeat to progress.
 */
public final class EnemyRoom implements RoomBehavior {
    private final Enemy enemy;
    private Double lifePoints;
    private final Random random = new Random();

    /**
     * Constructs an enemy room with the specified enemy.
     * 
     * @param enemy the enemy to be placed in this room
     */
    public EnemyRoom(final Enemy enemy) {
        this.enemy = enemy;
        this.lifePoints = enemy.health();
    }

    /**
     * Reduces the enemy's life points by the specified damage amount.
     * 
     * @param damage the amount of damage to be dealt to the enemy
     */
    public void takeDamage(final Double damage) {
        this.lifePoints = this.lifePoints - damage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateContent() {
        throw new UnsupportedOperationException("Unimplemented method 'generateContent'");
    }

    /**
     * {@inheritDoc}
     * Handles player interaction with the enemy room.
     * 
     * @param player    the player interacting with the room
     * @param direction the direction of interaction (1 for attack, other for
     *                  defense)
     */
    @Override
    public void interact(final Player player, final int direction) {
        if (direction == 1) {
            player.attack(this);
        } else {
            player.takeDamage(this.enemy.attacks().get(random.nextInt(this.enemy.attacks().size())).getY());
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getEnemyImagePath() {
        return this.enemy.spritePath();
    }

    /**
     * {@inheritDoc}
     */
    public Enemy getEnemy() {
        return this.enemy;
    }

    /**
     * {@inheritDoc}
     */
    public Double getAttackDamage() {
        return this.enemy.attacks().get(0).getY();
    }

    /**
     * Gets the current life points of the enemy.
     * 
     * @return the enemy's life points
     */
    public Double getLifePoints() {
        return this.lifePoints;
    }

    /**
     * Gets the name of the enemy in this room.
     * 
     * @return the enemy's name
     */
    public String getName() {
        return this.enemy.name();
    }
}
