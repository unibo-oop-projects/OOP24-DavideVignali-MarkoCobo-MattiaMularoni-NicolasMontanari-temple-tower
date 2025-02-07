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
 * @param xpsProbability the probability of the treasure containing experience points
 * @param enemyProbability the probability of the treasure containing an enemy
 * @param weaponProbability the probability of the treasure containing a weapon
 * @return a treasure room with the specified probabilities
 */
public class TreasureRoom implements RoomBehavior{

    private final Random random = new Random();

    //possible outcomes of the treasure
    private Optional<Integer> xps;
    private Optional<Enemy> enemy;
    private Optional<Weapon> weapon;
    private final int level;

    public TreasureRoom(int level, Optional<Weapon> weapon, double xpsProbability, double enemyProbability, double weaponProbability) {
        this.weapon = weapon;
        this.level = level;
        this.probabilisticRunner(List.of(xpsProbability, enemyProbability, weaponProbability), List.of(() -> generateTreasureOutcome("xps"), () -> generateTreasureOutcome("enemy"), () -> generateTreasureOutcome("weapon")));
    }

    /* 
     * Run one of probabilistic action on list based on the given probabilities.
     */
    private void probabilisticRunner(List<Double> probabilities, List<Runnable> actions){
        double cumulativeProbability = probabilities.stream().mapToDouble(mapper -> mapper).sum();
        if (Math.abs(cumulativeProbability - 1.0) > 1e-6) {
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

    private Enemy generateEnemy(){
        List<Pair<String,Double>> attacks = new ArrayList<>();
        attacks.add(new Pair<>("magical", 10.0));
        Map<String,Double> damageMultipliers = new HashMap<>();
        damageMultipliers.put("magical", 1.0);
        return new Enemy("Treasure_enemy", 20.0, this.level, attacks, damageMultipliers, "");
    }

    private String generateTreasureOutcome(final String outcome){
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

    @Override
    public void interact(Player player, int direction) {
        if(this.enemy.isPresent()){
            player.takeDamage(this.enemy.get().attacks().get(0).getY());
        } else if(this.weapon.isPresent()){
            player.changeWeapon(this.weapon.get());
        } else if(this.xps.isPresent()){
            player.increaseExperience(this.xps.get());
        }
    }

    @Override
    public void generateContent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateContent'");
    }
    
}
