package it.unibo.templetower.model;

public class PlayerImpl implements Player {

    private Weapon weapon;
    private int life;

    
    public PlayerImpl(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public void attack(EnemyRoom enemy) {
        if ( enemy != null) {
            enemy.takeDamage(weapon.getDamage());
        }
    }

    public void takeDamage(int damage){
        this.life = this.life - damage;
    }

    @Override
    public void changeWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public void chooseMove() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'chooseMove'");
    }

    @Override
    public void moveForward() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveForward'");
    }

    @Override
    public void moveBackward() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveBackward'");
    }

    @Override
    public int getHealth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHealth'");
    }

    @Override
    public int getExperience() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExperience'");
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    public int getLife() {
        return this.life;
    }

    
}
