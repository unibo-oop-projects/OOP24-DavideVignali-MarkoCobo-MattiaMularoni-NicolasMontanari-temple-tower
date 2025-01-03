package it.unibo.templetower.model;

public class EnemyImpl implements Enemy {
    int lifePoints = 0 ;
    public EnemyImpl(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    @Override
    public void attack(Player player) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'attack'");
    }

    @Override
    public void takeDamage(int damage) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'takeDamage'");
    }

    public int getLifePoints(){
        return this.lifePoints;
    }
    
}
