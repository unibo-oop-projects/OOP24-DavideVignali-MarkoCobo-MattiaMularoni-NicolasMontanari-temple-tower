package it.unibo.templetower.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import it.unibo.templetower.utils.Pair;

/**
 * Represents a room in the game that contains a treasure chest.
 * The treasure room can contain experience points, a weapon, or an enemy.
 */
public final class TreasureRoom implements RoomBehavior {

    private static final double EPSILON = 1e-6;
    private static final double ENEMY_BASE_HEALTH = 20.0;
    private static final double ENEMY_ATTACK_DAMAGE = 10.0;
    private final Random random = new Random();

    //possible outcomes of the treasure
    private Optional<Integer> xps;
    private Optional<Enemy> enemy;
    private Optional<Weapon> weapon;
    private final int level;

    /**
     * Creates a new treasure room with specified probabilities for different outcomes.
     * @param level the level of the room
     * @param weapon the optional weapon that could be found
     * @param xpsProbability probability of finding experience points
     * @param enemyProbability probability of encountering an enemy
     * @param weaponProbability probability of finding a weapon
     */
    public TreasureRoom(
            final int level, 
            final Optional<Weapon> weapon, 
            final double xpsProbability, 
            final double enemyProbability, 
            final double weaponProbability) {
        this.weapon = weapon;
        this.level = level;
        this.probabilisticRunner(
            List.of(xpsProbability, enemyProbability, weaponProbability),
            List.of(() -> generateTreasureOutcome("xps"),
                   () -> generateTreasureOutcome("enemy"),
                   () -> generateTreasureOutcome("weapon")));
    }

    /* 
     * Run one of probabilistic action on list based on the given probabilities.
     */
    private void probabilisticRunner(final List<Double> probabilities, final List<Runnable> actions) {
        double cumulativeProbability = probabilities.stream().mapToDouble(mapper -> mapper).sum();
        if (Math.abs(cumulativeProbability - 1.0) > EPSILON) {
            throw new IllegalArgumentException("Probabilities must sum to 1");
        }

        double roll = random.nextDouble();
        double cumulative = 0;
        for (int i = 0; i < probabilities.size(); i++) {
            cumulative += probabilities.get(i);
            if (roll < cumulative) {
                actions.get(i).run();
                return;
            }
        }
    }

    private Enemy generateEnemy() {
        List<Pair<String, Double>> attacks = new ArrayList<>();
        attacks.add(new Pair<>("magical", ENEMY_ATTACK_DAMAGE));
        Map<String, Double> damageMultipliers = new HashMap<>();
        damageMultipliers.put("magical", 1.0);
        return new Enemy("Treasure_enemy", ENEMY_BASE_HEALTH, this.level, attacks, damageMultipliers, "");
    }

    private String generateTreasureOutcome(final String outcome) {
        switch (outcome) {
            case "xps" -> {
                this.xps = Optional.of(random.nextInt(100));
                this.enemy = Optional.empty();
                this.weapon = Optional.empty();
                return "xps";
            }
            case "enemy" -> {
                this.enemy = Optional.of(generateEnemy());
                this.xps = Optional.empty();
                this.weapon = Optional.empty();
                return "enemy";
            }
            case "weapon" -> {
                this.weapon = Optional.of(weapon.get());
                this.xps = Optional.empty();
                this.enemy = Optional.empty();
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
        if (this.enemy.isPresent()) {
            player.takeDamage(this.enemy.get().attacks().get(0).getY());
        } else if (this.weapon.isPresent()) {
            player.changeWeapon(this.weapon.get());
        } else if (this.xps.isPresent()) {
            player.increaseExperience(this.xps.get());
        }
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
