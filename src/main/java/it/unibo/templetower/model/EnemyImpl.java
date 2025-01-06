package it.unibo.templetower.model;

public class EnemyImpl implements RoomBehavior {
    private int lifePoints;
    private int attackDamage;

    public EnemyImpl(int lifePoints, int attackDamage) {
        this.lifePoints = lifePoints;
        this.attackDamage = attackDamage;
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

    public int getAttackDamage() {
        return this.attackDamage;
    }

    public int getLifePoints(){
        return this.lifePoints;
    }
    
}
