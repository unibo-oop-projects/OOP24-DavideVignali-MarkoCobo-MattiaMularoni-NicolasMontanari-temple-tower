package it.unibo.templetower.model;

public class EnemyRoom implements RoomBehavior {
    private Double lifePoints;
    private final int attackDamage;

    public EnemyRoom(Double lifePoints, int attackDamage) {
        this.lifePoints = lifePoints;
        this.attackDamage = attackDamage;
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
        player.takeDamage(attackDamage);
    }

    public int getAttackDamage() {
        return this.attackDamage;
    }

    public Double getLifePoints(){
        return this.lifePoints;
    }
    
}
