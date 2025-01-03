package it.unibo.templetower.model;

public interface Enemy {
    public void attack(Player player);
    void takeDamage(int damage);
}
