package it.unibo.templetower.model;

public interface Player {
    void attack(EnemyRoom enemy);
    void changeWeapon(Weapon weapon);
    void chooseMove();
    void moveForward();
    void moveBackward();
    int getHealth();
    int getExperience();
    void getDamage(int damage);
    void increaseExperience(int xp);
}
