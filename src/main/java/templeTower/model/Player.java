package templeTower.model;

public interface Player {
    void attack(Enemy enemy);
    void changeWeapon(Weapon weapon);
    void chooseMove();
    void moveForward();
    void moveBackward();
    int getHealth();
    int getExperience();
}
