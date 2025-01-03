package it.unibo.templetower.model;

public class Trap implements RoomBehavior{
    private final int damage;

    public Trap(final int damage){
        this.damage = damage;
    }

    @Override
    public void interact(Player player) {
        player.getDamage(this.damage);
    }

}
