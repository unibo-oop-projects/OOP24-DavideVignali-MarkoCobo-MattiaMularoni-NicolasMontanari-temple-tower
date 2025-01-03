package it.unibo.templetower.model;

public class WeaponImpl implements Weapon {
    private int damage = 0;
    @Override
    public int getDamage() {
        return this.damage;
    }
    
}
