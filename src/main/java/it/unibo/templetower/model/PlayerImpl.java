package it.unibo.templetower.model;

public class PlayerImpl implements Player {

    @Override
    public void attack(Enemy enemy) {
        int lifeEnemy = enemy.getLifePoints();
    }

    @Override
    public void changeWeapon(Weapon weapon) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeWeapon'");
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
}
