package it.unibo.templetower.model;
import java.util.List;

import it.unibo.templetower.controller.GameDataManagerImpl;
import it.unibo.templetower.utils.Pair;

/**
 * Represents a room in the game that contains an enemy.
 * The enemy room contains an enemy that the player must defeat to progress.
 */
public class EnemyRoom implements RoomBehavior {
    private Enemy enemy;
    private FloorData floor;
    private Double lifePoints = 100.0;

    public EnemyRoom(GameDataManagerImpl gameDataManager, int floorIndex) {
        List<FloorData> floors = gameDataManager.getFloors();
        if (floorIndex < 0 || floorIndex >= floors.size()) {
            throw new IllegalArgumentException("Invalid floor level");
        }
        this.floor = floors.get(floorIndex);

        Pair<Integer, Integer> spawnRange = floor.spawningRange();
        int minLevel = spawnRange.getX();
        int maxLevel = spawnRange.getY();
        
        this.enemy = floor.enemies().get().stream()
            .filter(en -> (en.level() >= minLevel && en.level() <= maxLevel)).findFirst().orElseThrow();
            
    }
    
    public void takeDamage(Double damage){
        this.lifePoints = this.lifePoints - damage;
    }
    
    @Override
    public void generateContent() {
        throw new UnsupportedOperationException("Unimplemented method 'generateContent'");
    }

    @Override
    public void interact(Player player) {
        System.out.println("Enemy attack Player");
        player.takeDamage(enemy.attacks().get(0).getY());
    }

    public double getAttackDamage() {
        return this.enemy.attacks().get(0).getY();
    }

    public Double getLifePoints(){
        return this.lifePoints;
    }
    
}
