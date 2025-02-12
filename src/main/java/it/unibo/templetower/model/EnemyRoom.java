package it.unibo.templetower.model;

import java.util.Random;

/**
 * Represents a room in the game that contains an enemy.
 * The enemy room contains an enemy that the player must defeat to progress.
 */
public class EnemyRoom implements RoomBehavior {
    private final Enemy enemy;
    private Double lifePoints;
    
    public EnemyRoom(Enemy enemy){
        this.enemy = enemy;
        this.lifePoints = enemy.health();
    }

    public void takeDamage(Double damage){
        this.lifePoints = lifePoints - damage;
    }
    
    @Override
    public void generateContent() {
        throw new UnsupportedOperationException("Unimplemented method 'generateContent'");
    }

    @Override
    public void interact(Player player, int direction) {
        if (direction == 1) {
            player.attack(this);
            System.out.println("enemy life :"+ enemy.health());
            System.out.println("player life :"+ player.getLife());
        }else{
            Random random = new Random();
            player.takeDamage(this.enemy.attacks().get(random.nextInt(this.enemy.attacks().size())).getY());
        }
    }

    public String getEnemyImagePath(){
        return this.enemy.spritePath();
    }

    public Enemy getEnemy(){
        return this.enemy;
    }

    public Double getAttackDamage() {
        return this.enemy.attacks().get(0).getY();
    }

    public Double getLifePoints(){
        return this.lifePoints;
    }

    public String getName() {
        return this.enemy.name();
    }
    
}
