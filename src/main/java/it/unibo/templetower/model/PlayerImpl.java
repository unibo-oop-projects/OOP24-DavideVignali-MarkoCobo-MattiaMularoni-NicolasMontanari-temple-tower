package it.unibo.templetower.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Player interface representing a player in the game.
 * The player has weapons, life points, and can move between rooms.
 */
public final class PlayerImpl implements Player {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerImpl.class);
    private final List<Weapon> weapon;
    private double life;
    private Optional<Room> actualRoom;
    private int experience;
    private int actualWeaponIndex;

    /**
     * Creates a new player with initial weapon and room.
     * 
     * @param startweapon the initial weapon for the player
     * @param actualRoom  the starting room
     */
    public PlayerImpl(final Weapon startweapon, final Optional<Room> actualRoom) {
        this.weapon = new ArrayList<>();
        if (actualRoom.isEmpty()) {
            this.actualRoom = Optional.empty();
        } else {
            this.actualRoom = Optional.of(actualRoom.get());
        }
        weapon.add(startweapon);
        this.life = 100;
        this.experience = 0;
        this.actualWeaponIndex = 0;
    }

    @Override
    public void attack(final EnemyRoom enemy) {
        if (enemy != null) {
            enemy.takeDamage(weapon.get(actualWeaponIndex).attack().getY());
        }
    }

    @Override
    public void takeDamage(final double damage) {
        LOGGER.info("Player got damaged");
        this.life = this.life - damage;
    }

    /**
     * Adds a new weapon to the player's inventory.
     * If inventory is full (4 weapons), removes the first weapon.
     * 
     * @param newWeapon the weapon to add
     */
    public void addWeapon(final Weapon newWeapon) {
        this.weapon.add(newWeapon);
        if (this.weapon.size() == 4) {
            this.weapon.remove(0);
        }
    }

    @Override
    public void changeWeapon(final Weapon weapon) {
        LOGGER.info("Player changed weapon");
        this.actualWeaponIndex += 1;
        if (this.actualWeaponIndex == 3) {
            this.actualWeaponIndex = 0;
        }
    }

    @Override
    public void changeRoom(final Room room) {
        LOGGER.info("Player changed room: {}", room.getId());
        this.actualRoom = Optional.of(room);
    }

    @Override
    public int getExperience() {
        return this.experience;
    }

    /**
     * Gets the currently equipped weapon.
     * 
     * @return the current weapon
     */
    @Override
    public Weapon getActualWeapon() {
        return this.weapon.get(actualWeaponIndex);
    }

    /**
     * Gets the player's current life points.
     * 
     * @return the current life points
     */
    @Override
    public double getLife() {
        return this.life;
    }

    @Override
    public void increaseExperience(final int xp) {
        LOGGER.info("Player increased experience");
        this.experience += xp;
    }

    @Override
    public int getActualRoom() {
        if (actualRoom.isEmpty()) {
            return -1;
        } else {
            return actualRoom.get().getId();
        }
    }
}
