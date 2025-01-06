package it.unibo.templetower.model;

public class EnemyImpl implements RoomBehavior {
    private int lifePoints;
    private int attackDamage;

    public EnemyImpl(int lifePoints, int attackDamage) {
        this.lifePoints = lifePoints;
        this.attackDamage = attackDamage;
    }

    public int getLifePoints(){
        return this.lifePoints;
    }
    
    public void takeDamage(int damage){
        this.lifePoints = this.lifePoints - damage;
    }
    @Override
    public void generateContent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateContent'");
    }

    @Override
    public void interact(Player player) {
        System.out.println("Enemy attack Player");
        player.takeDamage(attackDamage);
    }

    @Override
    public EnemyImpl getEnemy() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnemy'");
    }
    
}
