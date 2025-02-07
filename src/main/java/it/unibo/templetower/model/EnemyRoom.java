package it.unibo.templetower.model;

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
        this.lifePoints = this.lifePoints - damage;
    }
    
    @Override
    public void generateContent() {
        throw new UnsupportedOperationException("Unimplemented method 'generateContent'");
    }

    @Override
    public void interact(Player player, int direction) {
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
