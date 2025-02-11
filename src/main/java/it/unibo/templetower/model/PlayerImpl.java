package it.unibo.templetower.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerImpl implements Player {

    private List<Weapon> weapon;
    private double life;
    private Optional<Room> actualRoom;
    private int experience;
    private int actualWeaponIndex;

    public PlayerImpl(final Weapon startweapon, final Optional<Room> actualRoom) {
        this.weapon = new ArrayList<>();
        if (actualRoom.isEmpty()) {
            this.actualRoom = Optional.empty();
        }else{
            this.actualRoom = Optional.of(actualRoom.get());
        }
        weapon.add(startweapon);
        this.life = 100;
        this.experience = 0;
        this.actualWeaponIndex = 0;
    }

    @Override
    public void attack(EnemyRoom enemy) {
        if ( enemy != null) {
            enemy.takeDamage(weapon.get(actualWeaponIndex).attack().getY());
        }
    }

    @Override
    public void takeDamage(double damage){
        System.out.println("Player got damaged");
        this.life = this.life - damage;
    }

    public void addWeapon(Weapon newWeapon){
        this.weapon.add(newWeapon);
        if(this.weapon.size() == 4){
            this.weapon.remove(0);
        }
    }

    @Override
    public void changeWeapon(Weapon weapon) {
        System.out.println("Player changed weapon");
        this.actualWeaponIndex += 1;
        if(this.actualWeaponIndex == 3){
            this.actualWeaponIndex = 0;
        }
    }

    @Override
    public void changeRoom(Room room) {
        System.out.println("Player changed room: "+room.id);
        this.actualRoom = Optional.of(room);
    }

    @Override
    public int getExperience() {
        return this.experience;
    }

    public Weapon getActualWeapon() {
        return this.weapon.get(actualWeaponIndex);
    }

    public double getLife() {
        return this.life;
    }

    @Override
    public void increaseExperience(int xp) {
        System.out.println("Player increased experience");
        this.experience += xp;
    }

    @Override
    public int getActualRoom() {
        if (actualRoom.isEmpty()) {
            return -1;
        }else{
            return actualRoom.get().id;
        }
    }
}
