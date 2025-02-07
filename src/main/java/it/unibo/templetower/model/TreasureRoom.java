package it.unibo.templetower.model;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import it.unibo.templetower.controller.GameDataManagerImpl;

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

    private FloorData floor;

    public TreasureRoom(GameDataManagerImpl gameDataManager, int floorIndex, double xpsProbability, double enemyProbability, double weaponProbability) {

        List<FloorData> floors = gameDataManager.getFloors();
        if (floorIndex < 0 || floorIndex >= floors.size()) {
            throw new IllegalArgumentException("Invalid floor level");
        }
        this.floor = floors.get(floorIndex);

        this.probabilisticRunner(List.of(xpsProbability, enemyProbability, weaponProbability), List.of(() -> generateTreasureOutcome("xps"), () -> generateTreasureOutcome("enemy"), () -> generateTreasureOutcome("weapon")));
    }
    public TreasureRoom(Optional<Weapon> weapon){
        //to implement
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

    private void generateTreasureOutcome(final String outcome){
        switch (outcome) {
            case "xps" -> {
                this.xps = Optional.of(random.nextInt(100));
                this.enemy = Optional.empty();
                this.weapon = Optional.empty();
            }
            case "enemy" -> {
                this.enemy = this.floor.enemies().get().stream().findAny();
                this.xps = Optional.empty();
                this.weapon = Optional.empty();
            }
            case "weapon" -> {
                this.weapon = this.floor.weapons().get().stream().findAny();
                this.xps = Optional.empty();
                this.enemy = Optional.empty();
            }
            default -> throw new AssertionError();
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
