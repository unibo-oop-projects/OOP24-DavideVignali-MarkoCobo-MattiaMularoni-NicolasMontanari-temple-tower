package it.unibo.templetower.model;

public class Trap implements RoomBehavior {
    private int damage;
    
    public Trap(int damage) {
        this.damage = damage;
    }

    @Override
    public void generateContent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateContent'");
    }

    @Override
    public void interact(Player player) {
        System.out.println("Player take a trap");
        player.takeDamage(damage);
    }

    public int getDamage() {
        return this.damage;
    }
    
}
