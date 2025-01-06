package it.unibo.templetower.model;

public interface Player {
    /**
     * @param enemy
     */
    void attack(EnemyImpl enemy);

    /**
     * @param weapon
     */
    void changeWeapon(Weapon weapon);

    /**
     * @param damage
     */
    void takeDamage(int damage);

    /**
     * 
     */
    void chooseMove();

    /**
     * 
     */
    void moveForward();

    /**
     * 
     */
    void moveBackward();

    /**
     * @return
     */
    int getHealth();

    /**
     * @return
     */
    int getExperience();
}
