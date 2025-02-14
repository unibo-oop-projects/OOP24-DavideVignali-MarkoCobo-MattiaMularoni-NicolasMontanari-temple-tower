package it.unibo.templetower.model;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Represents a room in the game that contains a treasure chest.
 * The treasure room can contain experience points, a weapon, or an enemy.
 */
public final class TreasureRoom implements RoomBehavior {

    private static final double EPSILON = 1e-6;
    private int indexElement; 
    private final Random random = new Random();

    //possible outcomes of the treasure
    private Optional<Integer> xps;
    private Optional<Weapon> weapon;

    /**
     * Creates a new treasure room with specified probabilities for different outcomes.
     * @param weapon the optional weapon that could be found
     * @param xpsProbability probability of finding experience points
     * @param weaponProbability probability of finding a weapon
     */
    public TreasureRoom(
            final Optional<Weapon> weapon, 
            final double xpsProbability, 
            final double weaponProbability) {
        this.weapon = weapon;
        this.probabilisticRunner(
            List.of(xpsProbability, weaponProbability),
            List.of(() -> generateTreasureOutcome("xps"),
                   () -> generateTreasureOutcome("weapon")));
    }

    /* 
     * Run one of probabilistic action on list based on the given probabilities.
     */
    private void probabilisticRunner(final List<Double> probabilities, final List<Runnable> actions) {
        final double cumulativeProbability = probabilities.stream().mapToDouble(mapper -> mapper).sum();
        if (Math.abs(cumulativeProbability - 1.0) > EPSILON) {
            throw new IllegalArgumentException("Probabilities must sum to 1");
        }

        final double roll = random.nextDouble();
        double cumulative = 0;
        for (int i = 0; i < probabilities.size(); i++) {
            cumulative += probabilities.get(i);
            if (roll < cumulative) {
                actions.get(i).run();
                return;
            }
        }
    }

    private String generateTreasureOutcome(final String outcome) {
        switch (outcome) {
            case "xps" -> {
                this.xps = Optional.of(random.nextInt(100));
                this.weapon = Optional.empty();
                return "xps";
            }
            case "weapon" -> {
                this.weapon = Optional.of(weapon.get());
                this.xps = Optional.empty();
                return "weapon";
            }
            default -> {
                return "empty";
            }
        }
    }

    /**
     * Handles player interaction with the treasure room.
     * @param player the player interacting with the room
     * @param direction the direction of interaction
     */
    @Override
    public void interact(final Player player, final int direction) {
        if (this.weapon.isPresent()) {
            indexElement = 1;
        } else if (this.xps.isPresent()) {
            player.increaseExperience(this.xps.get());
            indexElement = 2;
        }
    }

    @Override
    public int getElement() {
        return indexElement;
    }

    /**
     * 
     * @return the weapon in the treasure
     */
    public Weapon getWeapon() {
        return this.weapon.get();
    }

    /**
     * Generates the content for this room.
     * This method is currently not implemented.
     */
    @Override
    public void generateContent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateContent'");
    } 
}
