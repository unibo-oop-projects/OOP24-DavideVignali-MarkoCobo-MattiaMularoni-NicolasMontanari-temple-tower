package it.unibo.templetower.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a room in the game that contains an enemy.
 * The enemy room contains an enemy that the player must defeat to progress.
 */
public class EnemyRoom implements RoomBehavior {
    private static final Logger LOGGER = LogManager.getLogger(); 

    private final Enemy enemy;
    private Double lifePoints;
    
    public EnemyRoom(Enemy enemy){
        this.enemy = enemy;
        this.lifePoints = enemy.health();
    }

    public void takeDamage(Double damage){
        this.lifePoints = this.lifePoints - damage;
    }
    
    @Override
    public void generateContent() {
        throw new UnsupportedOperationException("Unimplemented method 'generateContent'");
    }

    @Override
    public void interact(Player player, int direction) {
        if (direction == 1) {
            LOGGER.info("Player attacks enemy");
            player.attack(this);
        }else{
            LOGGER.info("Enemy attacks player");
            player.takeDamage(this.enemy.attacks().get(0).getY());
        }
    }

    public double getAttackDamage() {
        return this.enemy.attacks().get(0).getY();
    }

    public Double getLifePoints(){
        return this.lifePoints;
    }

    public String getName() {
        return this.enemy.name();
    }
    
}
