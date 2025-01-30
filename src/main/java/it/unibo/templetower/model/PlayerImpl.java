package it.unibo.templetower.model;

public class PlayerImpl implements Player {

    private Weapon weapon;
    private double life;
    private Room actualRoom;
    private int index;

    
    public PlayerImpl(final Weapon weapon, final Room actualRoom) {
        this.weapon = weapon;
        this.actualRoom = actualRoom;
        this.life = 12;
    }

    @Override
    public void attack(EnemyRoom enemy) {
        if ( enemy != null) {
            enemy.takeDamage(weapon.attack().getY());
        }
    }

    @Override
    public void takeDamage(double damage){
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
    public void changeRoom(Room room) {
        this.actualRoom = room;
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

    public double getLife() {
        return this.life;
    }

    @Override
    public void increaseExperience(int xp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'increaseExperience'");
    }

    @Override
    public int getActualRoom() {
        return actualRoom.id;
    }

}
